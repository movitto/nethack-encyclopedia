#################################################
#
# Nethack Encyclopedia - parser
#
# Copyright (C) 2011: Mo Morsi <mo@morsi.org>
# Distributed under the MIT License
#
# Utility to extract NethackEncyclopedia articles from
# a nethack wiki xml dump.
#
# To use, simply run with the xml dump as the only param:
#
#   ruby parser.rb nethack-dump.xml
#
#################################################

require 'rubygems'
require 'rexml/document'
require 'wikicloth'

IGNORE_FILENAME_PATTERNS  =
  [/^Talk:.*/,   /^User:.*/, /^User talk:.*/, /^Blog:.*/,
   /^File:.*/, /^File talk:.*/, /^Forum:.*/,  /^Source:.*/,
   /^Source talk:.*/, /^.*\.c/, /^.*\.h/,

   # FIXME these should not be ignored but they break the wikicloth parser:
   /^Template.*/, /.*\/Archive.*/, /^Custom map symbols.*/, /^Enlightenment.*/,
   /^Human \(monster attribute\).*/, /^Hallucinatory messages.*/, /^Magic flute.*/]

MAX_FILE_SIZE = 500000 # in bytes

REGISTRY_DELIM=5.chr   # some random non-printable ascii

if ARGV.size != 1 || !File.exist?(ARGV[0])
  puts "Must specify valid xml source on the command line"
  exit 1
end

# parse the input document
doc = REXML::Document.new(File.read(ARGV[0]))
puts "Starting traversal"

# pull a articles out of the document and covert
i, registry_count, pos = 0, 0, 0
registry_file, output_file =  File.open("split/registry", "w"), nil
pages = doc.root.children.
          select { |c|
            c.node_type != :text && c.name == "page" && !c.elements['title'].nil? &&
            IGNORE_FILENAME_PATTERNS.select { |fn| c.elements['title'].text =~ fn }.empty? }.
          sort   { |c1,c2| c1.elements['title'].text <=> c2.elements['title'].text }

pages.each { |p|
  unless File.exist?("split/#{registry_count}") && (File.size("split/#{registry_count}") < MAX_FILE_SIZE)
    puts "Closing previous catalog file ##{registry_count}, opening new ##{registry_count += 1}"
    output_file = File.open("split/#{registry_count}", "w")
    pos = 0
  end

  puts "Pulling article '#{p.elements['title'].text}' (#{i+=1}/#{pages.size}) out of source"
  text = WikiCloth::Parser.new(:data => p.elements['revision'].elements['text'].text).to_html.strip.gsub(/\s+/, ' ')

  output_file.write   text
  registry_file.write "#{p.elements['title'].text}#{REGISTRY_DELIM}" +
                      "#{registry_count}#{REGISTRY_DELIM}" +
                      "#{pos}#{REGISTRY_DELIM}#{pos += text.size}#{REGISTRY_DELIM}"
}

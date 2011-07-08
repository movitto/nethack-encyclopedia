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
require 'rexml/document' # FIXME would rather use a libxml2 based parser

IGNORE_FILENAME_PATTERNS  = 
  [/^Talk:.*/,   /^User:.*/, /^User talk:.*/, /^Blog:.*/,
   /^File:.*/, /^File talk:.*/, /^Forum:.*/,  /^Source:.*/,
   /^Source talk:.*/, /^.*\.c/, /^.*\.h/]

MAX_FILE_SIZE = 800000 # in bytes

if ARGV.size != 1 || !File.exist?(ARGV[0])
  puts "Must specify valid xml source on the command line"
  exit 1
end

# parse the input document
doc = REXML::Document.new(File.read(ARGV[0]))
puts "Starting traversal"

# pull a sorted list of pages out of the document
registry = []
pages = REXML::XPath.match(doc, "/mediawiki/page")
pages.each { |p| 
  title, text = p.elements['title'], p.elements['revision'].elements['text']
  unless title.nil? || text.nil? ||
    !IGNORE_FILENAME_PATTERNS.select { |fn| title.text =~ fn }.empty?
      puts "Pulling article '#{title.text}' out of source"
      i = registry.size
      i-= 1 while (i-1) >= 0 && registry[i-1]['title'] > title.text
      registry.insert(i, {})
      registry[i]['title'] = title.text
      registry[i]['text']  = text.text
  end
}

# write the pages to the fs
i,r,total = 0,0,registry.size
registry_file, output_file = File.open("split/registry", "w"), nil
registry.each { |ri|
  title, text = ri['title'], ri['text']
  unless File.exist?("split/#{r}.xml") && (File.size("split/#{r}.xml") < MAX_FILE_SIZE)
    puts "Closing previous catalog file ##{r}.xml, opening new ##{r += 1}.xml"
    output_file.write "</pages>" unless output_file.nil?
    output_file = File.open("split/#{r}.xml", "w")
    output_file.write "<pages>\n"
  end
  puts "Processing page ##{i+=1}(#{title}) out of ##{total}, writing to split/#{r}.xml"
  output_file.write "<page name='#{REXML::Text.normalize(title)}'>\n#{REXML::Text.normalize(text)}\n</page>\n"
  registry_file.write "#{title}:#{r}\n"
}

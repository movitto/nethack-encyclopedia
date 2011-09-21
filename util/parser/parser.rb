############################################################
#                                                          #
# Nethack Encyclopedia - parser                            #
#                                                          #
# Copyright (C) 2011: Mo Morsi <mo@morsi.org>              #
# Distributed under the MIT License                        #
#                                                          #
# Utility to generate the encyclopedia content             #
#  from the nethack wiki                                   #
#                                                          #
# To use, simply run without any arguments:                #
#   ruby parser.rb                                         #
#                                                          #
############################################################

require 'rubygems'
require 'fileutils'
require 'nokogiri'
require 'curb'

############################################################

# constants to retrieve / parse nethack snapshot
MW_URL      = "http://nethack.wikia.com/wiki/"
MW_NS       = "http://www.mediawiki.org/xml/export-0.4/"
MW_SNAPSHOT = "http://wikistats.wikia.com/n/ne/nethack/pages_current.xml.gz"

# articles to ignore in the app
IGNORE_FILENAME_PATTERNS  =
  [/^Talk:.*/, /^Template.*/, /.*\/Archive.*/,
   /^User:.*/, /^User talk:.*/, /^User blog:.*/,
   /^Blog:.*/, /^File:.*/, /^File talk:.*/, /^Forum:.*/,
   /^Source:.*/, /^Source talk:.*/, /^.*\.c/, /^.*\.h/,
   /^Wikihack:.*/, /^Wikihack talk:.*/ ]

# paginate encyclopedia to conform to android restrictions
MAX_FILE_SIZE = 500000

# encyclopia hash table deliminiter
REGISTRY_DELIM=5.chr

# current working directory
WORKING_DIR = File.expand_path(File.dirname(__FILE__))

# actual location to store the encyclopedia
DST_DIR = "encyclopedia/"

# location which to store the wiki snapshot
WIKI_SNAPSHOT    = "snapshot.xml"
WIKI_SNAPSHOT_GZ = "snapshot.xml.gz"

# location which to make encyclopedia available so that
# Android can find it
ASSETS_DIR = '../../assets/encyclopedia'

# for enhanced output
COLORS = {:reset   => "\e[0m",
          :bold    => "\e[1m",
          :black   => "\e[30m",
          :red     => "\e[31m",
          :blue    => "\e[34m",
          :green   => "\e[32m"}

############################################################

# change working directory
Dir.chdir(WORKING_DIR)

# create DST_DIR and link to it from ASSETS_DIR,
# ensure it is empty
Dir.mkdir DST_DIR unless File.exists?(DST_DIR)
File.symlink WORKING_DIR + "/" + DST_DIR, ASSETS_DIR unless File.exists?(ASSETS_DIR)
Dir[DST_DIR + "/*"].each { |f| FileUtils.rm(f) }

# retrieve the snapshot
unless File.exist?(WIKI_SNAPSHOT)
  printf "#{COLORS[:green]} Retrieving Snapshot..." ; STDOUT.flush
  File.open(WIKI_SNAPSHOT_GZ, "w") { |f|
    f.write Curl::Easy.http_get(MW_SNAPSHOT).body_str
  }
  `gunzip #{WIKI_SNAPSHOT_GZ}`
  printf "#{COLORS[:red]} Done\n"
end

i, registry_count, pos = 0, 0, 0
pages = []

# pull article titles out of the snapshot
printf "#{COLORS[:blue]} Parsing snapshot..." ; STDOUT.flush
doc = Nokogiri::XML(File.open(WIKI_SNAPSHOT))
doc.xpath("//mw:mediawiki/mw:page", 'mw' => MW_NS).each { |page|
    title = page.xpath('mw:title',  'mw' => MW_NS).text
    if IGNORE_FILENAME_PATTERNS.select { |ifp| title =~ ifp }.empty?
      # insertion sort
      j = pages.index { |p| p > title }
      j = 0 if j.nil?
      pages.insert j, title
    end
}
printf "#{COLORS[:red]} Done\n"

# retrieve all articles
#   (previously we were converting the wikitext in the snapshot,
#    but no wikitext -> html converter renders the output as well as mediawiki itself)
puts "#{COLORS[:green]} Retrieving wiki articles..."
registry_file, output_file =  File.open("#{DST_DIR}/registry", "w"), nil
pages.each { |title,data|
  unless File.exist?("#{DST_DIR}/#{registry_count}") && (File.size("#{DST_DIR}/#{registry_count}") < MAX_FILE_SIZE)
    puts "#{COLORS[:blue]}  Closing previous catalog file ##{registry_count}" unless registry_count == 0
    puts "#{COLORS[:blue]}  Opening new catalog file ##{registry_count += 1}"
    output_file = File.open("#{DST_DIR}/#{registry_count}", "w")
    pos = 0
  end

  puts "  #{COLORS[:green]}(#{Time.now.strftime('%I:%M:%S%p')}) Retrieving article (#{i+=1}/#{pages.size}): '#{title}'" ; STDOUT.flush
  text = Curl::Easy.http_get(MW_URL + title.gsub(/\s/, '_')).body_str
  text = Nokogiri::HTML(text).xpath("//div[@id='WikiaArticle']").inner_html
  text.gsub!(/\s+/, ' ')
  #text.gsub!(/\<img[^\>]*\>/, '') # FIXME need to pull in images
  output_file.write   text
  registry_file.write "#{title}#{REGISTRY_DELIM}" +
                      "#{registry_count}#{REGISTRY_DELIM}" +
                      "#{pos}#{REGISTRY_DELIM}#{pos += text.size}#{REGISTRY_DELIM}"
}

printf "#{COLORS[:blue]} Compressing encyclopedia..." ; STDOUT.flush
Dir[DST_DIR + "/*"].each { |f| `gzip #{f}` }
printf "#{COLORS[:red]} Done\n"

puts "#{COLORS[:bold]}Completed#{COLORS[:reset]}"

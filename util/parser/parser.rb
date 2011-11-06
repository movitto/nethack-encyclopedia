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
MW_URL      = "http://nethackwiki.com"

# articles to ignore in the app
IGNORE_FILENAME_PATTERNS  =
  [/^.*\.c/, /^.*\.h/, /^NetHack [0-9]\.[0-9].*/, /^All pages.*$/ ]

# paginate encyclopedia to conform to android restrictions
MAX_FILE_SIZE = 500000

# encyclopia hash table deliminiter
REGISTRY_DELIM=5.chr

# current working directory
WORKING_DIR = File.expand_path(File.dirname(__FILE__))

# actual location to store the encyclopedia
DST_DIR = "encyclopedia/"

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

printf "#{COLORS[:green]} Retrieving pages list..." ; STDOUT.flush

# retrieve Special::AllPages, pull the sectioned pages out of that
all_pages_pages = []
all_pages = Curl::Easy.http_get(MW_URL + "/wiki/Special:AllPages").body_str
n = Nokogiri::HTML(all_pages)
n.xpath("//td[@class='mw-allpages-alphaindexline']").each{ |first|
  second = first.next_sibling.next_sibling
  all_pages_pages << "from=#{first.children[0].inner_text.gsub(/\s/, '_')}&to=#{second.children[0].inner_text.gsub(/\s/, '_')}"
}

# retrieve the list of pages out of the section pages
pages = []
all_pages_pages.each { |app|
  all_pages = Curl::Easy.http_get(MW_URL + "/mediawiki/index.php?title=Special:AllPages&#{app}").body_str
  n = Nokogiri.HTML(all_pages)
  n = n.xpath("//table[@class='mw-allpages-table-chunk']")[0]
  n.xpath("//td/a").each { |a|
    page = a.inner_text
    if IGNORE_FILENAME_PATTERNS.select { |ifp| page =~ ifp }.empty?
      pages << page
    end
  }
}

i, registry_count, pos = 0, 0, 0

# retrieve all articles
#   (previously we were converting the wikitext in the snapshot,
#    but no wikitext -> html converter renders the output as well as mediawiki itself)
puts "#{COLORS[:green]} Retrieving wiki articles..."
registry_file, output_file =  File.open("#{DST_DIR}/registry", "w"), nil
pages.each { |title|
  unless File.exist?("#{DST_DIR}/#{registry_count}") && (File.size("#{DST_DIR}/#{registry_count}") < MAX_FILE_SIZE)
    puts "#{COLORS[:blue]}  Closing previous catalog file ##{registry_count}" unless registry_count == 0
    puts "#{COLORS[:blue]}  Opening new catalog file ##{registry_count += 1}"
    output_file = File.open("#{DST_DIR}/#{registry_count}", "w")
    pos = 0
  end

  puts "  #{COLORS[:green]}(#{Time.now.strftime('%I:%M:%S%p')}) Retrieving article (#{i+=1}/#{pages.size}): '#{title}'" ; STDOUT.flush
  text = Curl::Easy.http_get(MW_URL + '/wiki/' + title.gsub(/\s/, '_')).body_str
  parser = Nokogiri::HTML(text).xpath("//div[@id='bodyContent']")
  remove_content = []
  remove_content << parser.search("//div[@id='siteSub']").first
  remove_content << parser.search("//div[@id='contentSub']").first
  remove_content << parser.search("//div[@id='jump-to-nav']").first
  remove_content << parser.search("//div[@class='printfooter']").first
  remove_content << parser.search("//div[@class='visualClear']").first
  parser.search("//script").each { |s| remove_content << s }
  remove_content.each { |r| r.remove unless r.nil? }
  text = parser.inner_html
  text.gsub!(/\s+/, ' ')
  text.gsub!(/<!--\s*\/*[a-zA-Z0-9]+\s*-->/, '')
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

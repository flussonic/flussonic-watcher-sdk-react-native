require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package["name"]
  s.version      = package["version"]
  s.summary      = package['description']
  s.description  = package['description']
  s.author       = package['author']
  s.homepage     = package['homepage']
  s.license      = { :type => 'Copyright', :file => "LICENSE" }
  s.source       = { :git => "https://github.com/flussonic/flussonic-watcher-sdk-react.git", :tag => "v#{s.version}" }
  s.requires_arc = true
  s.static_framework = true

  s.preserve_paths = 'LICENSE', 'README.md', 'package.json', '*.js'
  s.source_files   = "ios/**/*.{h,m,swift}"
  s.exclude_files  = "ios/Frameworks/**/*"

  s.dependency 'React'
  s.dependency 'DynamicMobileVLCKit', '~> 3.3'
  s.dependency 'flussonic-watcher-sdk-ios', '~> 2.0'

  s.platform    = :ios, "9.3"
  s.swift_version = "5.0"
end

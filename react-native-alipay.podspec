require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-alipay"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "10.0" }
  s.source       = { :git => "https://github.com/kafudev/react-native-alipay.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm}"

  s.dependency "React-Core"
  s.vendored_frameworks = "ios/AlipaySDK/AlipaySDK.framework"
  s.resource = 'ios/AlipaySDK/AlipaySDK.bundle'
  # s.vendored_frameworks = "ios/AlipaySDK_noutdid/AlipaySDK.framework"
  # s.resource = 'ios/AlipaySDK_noutdid/AlipaySDK.bundle'
  s.frameworks = "SystemConfiguration", "CoreTelephony", "QuartzCore", "CoreText", "CoreGraphics", "UIKit", "Foundation", "CFNetwork", "CoreMotion"
  s.library = "c++", "z"
end

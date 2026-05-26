/*
* MIT License
*
* Copyright (c) 2026 Hridoy Chandra Das
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/
package template.common.util

import platform.Foundation.NSUserDefaults

actual object PlatformUtils {
    actual fun changeLanguage(code: String) {
        val defaults = NSUserDefaults.standardUserDefaults
        if (code.isEmpty()) {
            defaults.removeObjectForKey("AppleLanguages")
            defaults.removeObjectForKey("AppleLocale")
        } else {
            defaults.setObject(listOf(code), "AppleLanguages")
            defaults.setObject(code, "AppleLocale")
        }
        // Force immediate persistence for debug environments
        defaults.synchronize()
    }

    actual fun changeTheme(isDark: Boolean) {
        // No-op or implementation if needed for iOS
    }
}

## Greedy
a simple android image loading library developed in kotlin


#### Step1: Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```	
	
  
 #### Step2:  Add the dependency
 
 ```
 dependencies {
	        implementation 'com.github.bhanup212:Greedy:1.0'
	}
```	
  
  
  #### Usage:
  
  ```kotlin
  ImageLoader.load("https://homepages.cae.wisc.edu/~ece533/images/boat.png",imageview)
  ```    
      
      
  

  
  #### check this repo for reference:https://github.com/bhanup212/SubRedditImageViewer
  


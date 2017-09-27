
# similarLoadingView
A stylish loading view for Android


<img src="/usage.gif" alt="similarLoadingView" width= "250px"/>

## Implementation

similarLoadingView, is a custom loading view for Android. It is very easy to use and customize. It draws your drawable on canvas with stylish design.

## Gradle 
    compile 'similar.io.view:similarLoadingView:1.0.0'

## Maven
```xml
<dependency>
  <groupId>similar.io.view</groupId>
  <artifactId>similarLoadingView</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

## Sample App

<a href='https://play.google.com/store/apps/details?id=com.primemarin.similar.io&hl=en' target='_blank'><img height='50' style='border:0px;height:50px;' src='/GooglePlay.png' border='0' alt='GooglePlay Link' /></a>


## Usage
* In XML layout:
```
      <similar.io.view.SimilarLoadingView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true"
        app:similar_animDuration="5000"
        app:similar_cornerRadius="40"
        app:similar_loadingDrawable="@drawable/loading"
        app:similar_maxAlpha="225" />
     ```
	 
You can find which attributes you can use on xml below

| Attribute | Usage | 
| --- | --- |
| app:similar_loadingDrawable | @drawable/your_icon | 
| app:similar_animDuration | int millisecond Duration | 
| app:similar_cornerRadius | int radius of corners for each sliced image |  
| app:similar_maxAlpha | int alpha 0 to Max amount for each animation change |  

**v1.0.0**
- Initial release .

## Contact me
Any questions:Please feel free to contribute by pull request, issues or feature requests.
* Email: burak@mbakgun.com
* Linkedin: https://www.linkedin.com/in/mehmet-burak-akgün-90a341a4/

## License

    Copyright 2017 Mehmet Burak Akgün

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

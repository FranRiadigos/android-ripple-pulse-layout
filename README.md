# Ripple Pulse over RelativeLayout

This is an Android Custom Layout container that generates animated pulses.

![Demo][1]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![Android Studio Preview][2]

## Why choosing this library?

Other libraries add a [View][5] object into the container to make the same effect. This is costly, 
besides in case you execute `ViewGroup.removeAllViews()` the animation effect will disappear.

Instead, the **Ripple Pulse** library just draws a Paint layer over the canvas, 
leaving the container empty while doing a smooth animation.


## How to apply?

Latest stable version: [![Latest Version][3]][4]

Add to your dependencies

```groovy
repositories {
 
    jcenter()
    
    // Optional. Access to early versions.
    maven { url "https://dl.bintray.com/kuassivi/maven" }
    
}
 
dependencies{
 
    implementation 'com.kuassivi.android.view:ripple-pulse-layout:<latest version>'
    
}
```


## How to use?

Add to your layout

```xml
<com.kuassivi.component.RipplePulseRelativeLayout
        android:id="@+id/pulseLayout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:pulse_layout_RippleStartRadiusPercent="100"
        app:pulse_layout_RippleEndRadiusPercent="350"
        app:pulse_layout_ShowPreview="true"
        app:pulse_layout_PulseType="fill"
        app:pulse_layout_PulseDuration="1800">
        
        <!-- You can add now any View inside here -->
 
</com.kuassivi.component.RipplePulseRelativeLayout>
```

To start the animation


    pulseLayout.startPulse()


To stop the animation


    pulseLayout.stopPulse()


## Properties

`pulse_layout_ShowPreview` - Boolean. Indicates if the bounds of the animation are shown in the Preview.

`pulse_layout_RippleColor` - Color or reference. Indicates the color of the ripple pulse.

`pulse_layout_PulseType` - Enum. Indicates the style of the pulse, it can be fill or stroke.

`pulse_layout_PulseDuration` - Integer or reference. Indicates the duration of the pulse animation from the start to the end radius.

`pulse_layout_StartDelay` - Integer or reference. Indicates the duration to delay at the start of the pulse animation.

`pulse_layout_EndDelay` - Integer or reference. Indicates the duration to delay at the end of the pulse animation.

`pulse_layout_RippleStrokeWidth` - Integer or reference. Indicates the stroke width.

`pulse_layout_RippleStartRadiusPercent` - Float positive values. Indicates the start radius.

`pulse_layout_RippleEndRadiusPercent` - Float positive values. Indicates the end radius.

`pulse_layout_PulseInterpolator` - Reference. Indicates the interpolator for the animation.
 
## Contributions

You are welcome to contribute, just raise a ticket on the issues page, and make a pull request with your changes.

[1]: ./art/demo.gif
[2]: ./art/android-studio-preview.png
[3]: https://api.bintray.com/packages/kuassivi/maven/ripple-pulse-layout/images/download.svg
[4]: https://bintray.com/kuassivi/maven/ripple-pulse-layout/_latestVersion
[5]: https://developer.android.com/reference/android/view/View

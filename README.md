# Ripple Pulse over RelativeLayout

Android Custom View that acts like a pulse container wrapping any View.

![Demo][1]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![Android Studio Preview][2]

## Why to use this library?

Other libraries add a View object into the container to make the same effect. This is expensive memory consumption and in case you want to do a `ViewGroup.removeAllViews()` the animation will disappear because the View object will also gone.

Instead, **RipplePulseRelativeLayout** library just draw a Paint layer over the canvas, leaving the container empty while doing a smooth animation.


## Gradle

Latest stable version: 
[![Latest Version](https://api.bintray.com/packages/kuassivi/maven/ripple-pulse-layout/images/download.svg) ](https://bintray.com/kuassivi/maven/ripple-pulse-layout/_latestVersion)

Add this to your build.gradle:

```groovy
repositories {
 
    maven { url "https://dl.bintray.com/chattylabs/maven" }
    
}
 
dependencies{
 
    implementation 'com.kuassivi.android.view:ripple-pulse-layout:0.1.5'
    
}
```


## Usage

Add this to your layout:

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

To start the animation just do:

```java 
pulseLayout.startPulse()
```

To stop the animation just do:

```java 
pulseLayout.stopPulse()
```

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
 
## Contribute

You are welcome to contribute, just raise a ticket on the issues page, and make a pull request with your changes.

[1]: ./art/demo.gif
[2]: ./art/android-studio-preview.png

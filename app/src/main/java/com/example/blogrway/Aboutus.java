package com.example.blogrway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shashank.sony.fancyaboutpagelib.FancyAboutPage;

public class Aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        FancyAboutPage fancyAboutPage=findViewById(R.id.fancyaboutpage);
        //fancyAboutPage.setCoverTintColor(Color.BLUE);  //Optional
        fancyAboutPage.setCover(R.drawable.coverimg); //Pass your cover image
        fancyAboutPage.setName("Akshay Hadapad");
        fancyAboutPage.setDescription("Aspiring Android Developer | Android App,Web and Software Developer.");
        fancyAboutPage.setAppIcon(R.drawable.cakepop); //Pass your app icon image
        fancyAboutPage.setAppName("BlogrWay");
        fancyAboutPage.setVersionNameAsAppSubTitle("1.0.3");
        fancyAboutPage.setAppDescription("Write something awesome and feature on Top blogs of the week, get notified when your followed blogger posts something, small app size with auto updates within the app\n\n" +
                "Comment on blog you let the blogger know what you think about the blog, share the blog using our app, directly start a blog from your galley, simply share the image from you gallery directly to our app and start writing!\n\n"+
                "keep blogging \uD83D\uDE0A\uD83D\uDCDD");
        fancyAboutPage.addEmailLink("hadapadakshay@gmail.com");     //Add your email id
        fancyAboutPage.addFacebookLink("https://www.linkedin.com/in/hadapadakshay");  //Add your facebook address url
        fancyAboutPage.addTwitterLink("https://www.linkedin.com/in/hadapadakshay/");
        fancyAboutPage.addLinkedinLink("https://www.linkedin.com/in/hadapadakshay");
        fancyAboutPage.addGitHubLink("https://github.com/AkshayHadapad");
    }
}
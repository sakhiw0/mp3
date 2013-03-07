
//********************************//
// Author: 		Mtewnka Sakhiwo   //
// Tittle: 		MP3 Player        //
// Date:   		16 August 2011    //
// Student no:  mtwsak001         //
//********************************//


   package mp3.com;

//import all required packages
   import java.io.File;
   import java.io.IOException;

   import android.app.Activity;

   import android.media.AudioManager;
   import android.media.MediaPlayer;
   //import android.media.MediaPlayer.OnCompletionListener;
   import android.os.Bundle;
   import android.os.Handler;
   import android.view.MotionEvent;
   import android.view.View;
   import android.view.View.OnClickListener;
   import android.view.View.OnTouchListener;
   import android.widget.Button;
   import android.widget.SeekBar;
   import android.widget.TextView;

   
//class mp3
    public class mp3 extends Activity 
   {
   
      private String[] array = new String[2000]; // Array for the songs
      private String path = "/mnt/sdcard/LOST.DIR/"; // The directory  or path to the songs
      private Button PlayPause; // Play and pause button
      private Button stopB; // stop button
      private Button buttonN; // next button
      private Button buttonB; // back button
      private MediaPlayer mediaPlayer; // Object of mediaPlayer
      private SeekBar seekBar; // SeekBar
      private final Handler handler = new Handler(); // Event Handler
      private TextView Title; // TextView for the title
      private File mFile = new File(path); // add the path to the file object
      private int currentPosition = 0; // CurrentPosition for the songs
      private int count = 0; // count variable for the number of songs
   
   
   // used to create an activity
       @Override
       public void onCreate(Bundle savedInstanceState) 
      {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.main);
         check(mFile);
       
         mediaPlayer = new MediaPlayer();  
         Title = (TextView)findViewById(R.id.textView2);
         PlayPause  = (Button) findViewById(R.id.PlayPause);
         stopB = (Button)findViewById(R.id.stopB);
         buttonN = (Button)findViewById(R.id.buttonN);
         buttonB = (Button)findViewById(R.id.buttonB);
         PlayPause.setOnClickListener(
                new OnClickListener(){ 
                   public void onClick(View v){Play();}});
         sound(path);
         SeekBarState();
         stop();
      
        // add the next button to the action listener
         buttonN.setOnClickListener(
                new OnClickListener()
               { 
                   public void onClick(View v){
                     nextSong();
                     mediaPlayer.reset();
                     sound(path);
                     mediaPlayer.start();
                  }});
       
        // add the back button to the action action listener
         buttonB.setOnClickListener(
                new OnClickListener()
               { 
                   public void onClick(View v){
                     backSong();
                     mediaPlayer.reset();
                     sound(path);
                     mediaPlayer.start();
                  }});
      }
   
   // check method for listing all mp3 files from a directory
       public void check(File temp)
      {
      //
         if(temp.isDirectory())	 
            for(File subFolder : temp.listFiles())	
               check(subFolder);		 
         else	
         {
         // copy all mp3 files into an array
            if(temp.getName().endsWith(".mp3"))	
            {
               array[count] = temp.getName();
               count++;
            }
         }
      }// check()
   
   // stop method for stopping the mediaPlayer object
       private void stop()
      {
         stopB.setOnClickListener(
                new View.OnClickListener()
               {
                   public void onClick(View v){
                     {
                        mediaPlayer.reset();
                        sound(path);
                        PlayPause.setText(getString(R.string.play_str));
                        Title.setText(""); // set the song title to an empty space
                     }}});
      }// stop()
   
   
   // SeekBarState method for changing the seekbar 
       private void SeekBarState()
      {
         seekBar = (SeekBar)findViewById(R.id.seekBar01);
         seekBar.setMax(mediaPlayer.getDuration());
         seekBar.setOnTouchListener(
                new OnTouchListener(){ 
                   public boolean onTouch(View v,MotionEvent event){
                     ChangeBar(v);
                     return false;}});
      }// SeekBarState
   
   // Sound method for managing the sound
       public void sound(String argumentStr)
      {
         try{
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// open a Stream for the sound
            mediaPlayer.setDataSource(argumentStr + array[currentPosition]);// add the path for the song and the array for songs
            mediaPlayer.prepare(); // place prepare into try-catch block
         }
         
         // catch all the unexpected exceptions
             catch(IllegalArgumentException e)
            {
               e.printStackTrace();
            }
         
             catch(IllegalStateException e)
            {
               e.printStackTrace();
            }
         
             catch(IOException e){
               e.printStackTrace();
            }
      }// sound()
   
    // method to handle the seekbar when a song is playing
       public void StartProgress()
      {
         seekBar.setProgress(mediaPlayer.getCurrentPosition()); 
      
      // when the media player is playing star the seekbar
         if(mediaPlayer.isPlaying())
         {
            Runnable notification = 
                new Runnable()
               {
                   public void run ()
                  {
                     StartProgress();
                  }
               };
            handler.postDelayed(notification,1000);
         }
         else
         {
            mediaPlayer.pause();
         }
      }// StartProgressBar()
    
    // ChangeBar() 
       private void ChangeBar(View v)
      {
      // when the seekbar is playing, change the seekbar
         if(mediaPlayer.isPlaying())
         {
            SeekBar sb = (SeekBar)v;
            mediaPlayer.seekTo(sb.getProgress());
         }
      }// ChangeBar()
    
    // play()
       public void Play()
      {
      // when the button is play
         if(PlayPause.getText() == getString(R.string.play_str))
         {
            PlayPause.setText(getString(R.string.pause_str)); // set the button to PAUSE
         
         // try to play the song
            try
            {
               sound(path);
               mediaPlayer.start();
               StartProgress();
               Title.setText(array[currentPosition]);
            }
            
            //catch the IllegalStateException
                catch(IllegalStateException e)
               {
                  mediaPlayer.pause();
               }
         }
         
         //else set the button to Play
         else
         {
            PlayPause.setText(getString(R.string.play_str));
            mediaPlayer.pause();
            StartProgress();
         }
      }// Play()
    
    // NextSong method
       private void nextSong()
      {
      // when count is equal to zero, set currentPosition to 0
         if(++currentPosition == count)
         {
            currentPosition = 0; // set currentPosition to 0
            sound(path);
            StartProgress();
            Title.setText(array[currentPosition]); // set title to the current song
         }
         
         // else keep on going to the next song
         else
         {	
            StartProgress();
            Title.setText(array[currentPosition]);
            sound(path);
         }
         StartProgress();
      }// NextPlay()
    
    //BackSong method for playing previous song
       private void backSong()
      {
      	// when the current position is less than 
         if(mediaPlayer.getCurrentPosition() < count && currentPosition >=1)
         {
            sound(path);
            Title.setText("");
            mediaPlayer.start();
            StartProgress();
         }
         
         // else keep decrimenting when currentPosition is less thatn the number of songs
         else
         {
            if(currentPosition >= 0)
            {
               if(currentPosition > 0)
               {
                  --currentPosition;    							
                  Title.setText(array[currentPosition]);
                  StartProgress();
               }
               	
               // else currentPosition is equal to the last song on the array
               else
               {
                  currentPosition = count	-1;	
                  Title.setText(array[currentPosition]);
               }
               sound(path);
               mediaPlayer.start();
            }
         }
      }//  backSong()
   }// end of class

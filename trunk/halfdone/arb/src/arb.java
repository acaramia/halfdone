import processing.core.*; 
import processing.xml.*; 

import traer.physics.*; 
import traer.animation.*; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class arb extends PApplet {

final float NODE_SIZE = 10;
final float EDGE_LENGTH = 20;
final float EDGE_STRENGTH = 0.2f;
final float SPACER_STRENGTH = 1000;

ParticleSystem physics;
Smoother3D centroid;
Particle pmin,root;

// PROCESSING /////////////////////////////////////

public void setup()
{
  size( 400, 400 );
  smooth();
  frameRate( 24 );
  strokeWeight( 2 );
  ellipseMode( CENTER );       
  
  physics = new ParticleSystem( 0, 0.25f );
  centroid = new Smoother3D( 0.8f );
    
  initialize();
}

public void draw()
{
  physics.tick( 1.0f ); 
  if ( physics.numberOfParticles() > 1 )
    updateCentroid();
  centroid.tick();
  background( 255 );
  
  translate( width/2 , height/2 );
  scale( centroid.z() );
  translate( -centroid.x(), -centroid.y() );

  drawNetwork();  
}

public void drawNetwork()
{      
  //determina posizione mouse secondo trasformazioni
  //inverse del centroide
  float mx=mouseX;
  float my=mouseY;  
  mx=mx-width/2;
  my=my-height/2;
  mx=mx/centroid.z();
  my=my/centroid.z();
  mx=mx+centroid.x();
  my=my+centroid.y();
  fill(0,0,255);
  noStroke();
  ellipse( mx,my, NODE_SIZE, NODE_SIZE );
  
  pmin=physics.getParticle( 0);
  float d,dmin=Float.MAX_VALUE;

  // draw vertices
  fill( 160 );
  noStroke();
  for ( int i = 0; i < physics.numberOfParticles(); ++i )
  {
    Particle v = physics.getParticle( i );
    ellipse( v.position().x(), v.position().y(), NODE_SIZE, NODE_SIZE );
    //determina la particella a distanza minima dal mouse
    d=dist(v.position().x(), v.position().y(),mx,my);
    if(d<dmin){
      dmin=d;
      pmin=v;
      
    }
  }
  //in rosso il pmin
  fill( 255,0,0 );  
  ellipse( pmin.position().x(), pmin.position().y(), NODE_SIZE, NODE_SIZE );
  //in verde il root
  fill( 0,255,0 );  
  ellipse( root.position().x(), root.position().y(), NODE_SIZE, NODE_SIZE );
  
  // draw edges 
  stroke( 0 );
  beginShape( LINES );
  for ( int i = 0; i < physics.numberOfSprings(); ++i )
  {
    Spring e = physics.getSpring( i );
    Particle a = e.getOneEnd();
    Particle b = e.getTheOtherEnd();
    vertex( a.position().x(), a.position().y() );
    vertex( b.position().x(), b.position().y() );
  }
  endShape();
}

public void mousePressed() {
//  
}

public void keyPressed()
{
  if ( key == 'i' )
  {
    initialize();
    return;
  }  
  if ( key == '1' )
  {
    addNode("1");
    return;
  }  
  if ( key == '2' )
  {
    addNode("2");
    return;
  }
}

// ME ////////////////////////////////////////////

public void updateCentroid()
{
  float 
    xMax = Float.NEGATIVE_INFINITY, 
    xMin = Float.POSITIVE_INFINITY, 
    yMin = Float.POSITIVE_INFINITY, 
    yMax = Float.NEGATIVE_INFINITY;

  for ( int i = 0; i < physics.numberOfParticles(); ++i )
  {
    Particle p = physics.getParticle( i );
    xMax = max( xMax, p.position().x() );
    xMin = min( xMin, p.position().x() );
    yMin = min( yMin, p.position().y() );
    yMax = max( yMax, p.position().y() );
  }
  float deltaX = xMax-xMin;
  float deltaY = yMax-yMin;
  if ( deltaY > deltaX )
    centroid.setTarget( xMin + 0.5f*deltaX, yMin +0.5f*deltaY, height/(deltaY+50) );
  else
    centroid.setTarget( xMin + 0.5f*deltaX, yMin +0.5f*deltaY, width/(deltaX+50) );
}

public void addSpacersToNode( Particle p, Particle r )
{
  for ( int i = 0; i < physics.numberOfParticles(); ++i )
  {
    Particle q = physics.getParticle( i );
    if ( p != q && p != r )
      physics.makeAttraction( p, q, -SPACER_STRENGTH, 20 );
  }
}

public void makeEdgeBetween( Particle a, Particle b )
{
  physics.makeSpring( a, b, EDGE_STRENGTH, EDGE_STRENGTH, EDGE_LENGTH );
}

public void initialize()
{
  physics.clear();
  root=physics.makeParticle();
  centroid.setValue( 0, 0, 1.0f );
}

public void addNode(String s)
{ 
  Particle q=pmin;
  if(s.equals("1"))
    q=root;
    
  Particle p = physics.makeParticle();
  //Particle q = physics.getParticle( (int)random( 0, physics.numberOfParticles()-1) );
  //while ( q == p )
  //  q = physics.getParticle( (int)random( 0, physics.numberOfParticles()-1) );
  
  addSpacersToNode( p, q );
  makeEdgeBetween( p, q );
  p.moveTo( q.position().x() + random( -1, 1 ), q.position().y() + random( -1, 1 ), 0 );
}


  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "arb" });
  }
}

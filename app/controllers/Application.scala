package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api._
import models.Artist

class Application @Inject()(cc: ControllerComponents) 
 extends AbstractController(cc){

     def listArtist = Action{
         Ok(views.html.home(Artist.fetch))
     }

     def fetchArtistByName(name:String) = Action{
         Ok(views.html.home(Artist.fetchByName(name)))
     }

     def search(name:String,country:String) = Action{
         val result=Artist.fetchByNameOrCountry(name,country)
         if(result.isEmpty){
             NoContent
         }else{
             Ok(views.html.home(result))
         }
     }

     def searchTwo(name:Option[String],country:String)= Action{
         val result = name match {
             case Some(value) => Artist.fetchByNameOrCountry(value,country)
             case None => Artist.fetchByCountry(country)
         }
         if(result.isEmpty){
             NoContent
         }else{
             Ok(views.html.home(result))
         }
     }
 }
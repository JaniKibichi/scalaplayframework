package controllers

import models.Task
import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api._
import java.io.File

class AppController @Inject()(cc: ControllerComponents)
extends AbstractController(cc){

    def subscribe = Action(parse.text){ request =>
        Ok("Added" + request.body+ "to subscriber's list")
    }

    def subscribeJSON = Action(parse.json){ request =>
        val reqData: JsValue = request.body
        val email=(reqData \ "emailId").as[String]
        val interval=(reqData \ "interval").as[String]
        Ok(s"added $email to subscriber's list and will send updates every &interval")
    }

    def createProfile = Action(parse.multipartFormData){ request =>
        val formData = request.body.asFormUrlEncoded
        val email: String = formData.get("email").get(0)
        val name: String = formData.get("name").get(0)
        val userId: Long = User(email,name).save

        request.body.file("displayPic").map{ picture =>
            val path = "/socialize/user/"
            if(!picture.filename.isEmpty){
                picture.ref.moveTo(new File(path+userId+".jpeg"))
            }
            Ok("successfully added user")
        }.getOrElse{
            BadRequest("failed to add user")
        }
    }
}
package controllers

import models.Task
import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api._

class TaskController @Inject()(cc: ControllerComponents) extends AbstractController(cc){
    def index = Action{
        Redirect(routes.TaskController.tasks)
    }

    def tasks = Action{
        Ok(views.html.index(Task.all))
    }

    def newTask = Action(parse.urlFormEncoded){ implicit request =>
        Task.add(request.body.get("taskName").get.head)
        Redirect(routes.TaskController.index)
    }

    def deleteTask(id: Int) = Action{
        Task.delete(id)
        Ok
    }
}
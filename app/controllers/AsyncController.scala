package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.twirl.api._
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.concurrent.Future
import java.nio.channels.NoConnectionPendingException

class AsyncController @Inject()(cc: ControllerComponents)
extends AbstractController(cc){

    def getReport(fileName:String) = Action.async{
        Future{
            val file:File = new File(fileName)

            if(file.exists()){
                val info = file.lastModified()
                Ok(s"lastModified on ${new Date(info)}")
            }else{
                NoContent 
            }
        }
    }

    def getReportWaitTenSeconds(fileName:String) = Action.async{
        val mayBeFile = Future{
            new File(fileName)
        }

        val timeout = play.api.libs.concurrent.Promise.timeout("Past max time",10, TimeUnit.SECONDS)

        Future.firstCompletedOf(Seq(mayBeFile,timeout)).map{
            case f: File => 
                if(f.exists()){
                    val info = f.lastModified()
                    Ok(s"lastModified on ${new Date(info)}")
                }
                else
                    NoContent 
            case t: String => InternalServerError(t)
        }
    }
}
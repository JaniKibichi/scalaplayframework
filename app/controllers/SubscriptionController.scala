package controllers

class SubscriptionController @Inject()(cc: ControllerComponents) 
extends AbstractController(cc){

    val parseAsSubscription = parse.using{ request =>
        parse.json.map{ body =>
            val email:String = (body \"emailId").as[String]
            val fromDate:Long = (body \"fromDate").as[String]
            Subscription(email,fromDate)
        }
    }

    implicit val subWrites = Json.writes[Subscription]
    def getSub = Action(parseAsSubscription){ request =>
        val subscription: Subscription = request.body
        Ok(Json.toJson(subscription))
    }
}
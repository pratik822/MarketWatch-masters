package marketwatch.com.app.marketwatch.data

data class AddPostData(  val buyprice: String,
                         val stoploss: String,
                         val storename: String,
                         val target1: String,
                         val target2: String,
                         val target3: String,
                         val type: String,
                         var duration:String)

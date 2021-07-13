package app.jw.mapable.gm.community

data class ItemCommunityMain (
    val title : String,
    val content : String,
    val username : String,
    val posttime : String,
    val imageLink : String,

    val like : Long,
    val dislike : Long,
    val star : Long,

)
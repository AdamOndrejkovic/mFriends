package com.easv.oe.friends.Model


class Friends {

    val mFriends = arrayOf<BEFriend>(
        BEFriend("Simon", "123", true, "72 Willow Dr."),
        BEFriend("Dennis", "1234", false, "35 Strawberry St."),
        BEFriend("Mina", "12345", true, "43 Whitemarsh St."),
        BEFriend("Emil", "12345678", true,"455 Lakewood St."),
        BEFriend("Mads", "23456789", true,"5 Rosewood Lane"),
        BEFriend("Martin", "87654321", false, "9966 Greystone Lane"),
        BEFriend("Mike", "12121212", true, "8898 Country Club St."),
        BEFriend("Trine", "123", true, "4 North Liberty Street"),
        BEFriend("Mathias", "1234", false, "799 Center Ave."),
        BEFriend("Rasmus", "12345", true, "9304 West Bald Hill Drive"),
        BEFriend("Christian", "12345678", true, "736 James Rd."),
        BEFriend("Peter", "23456789", true, "841 Garfield St."),
        BEFriend("Anders", "87654321", false, "79 Cross St."),
        BEFriend("Mikkel", "12121212", true, "7673 Honey Creek Circle"),
        BEFriend("Flemming", "123", true, "7355 North Indian Spring Ave."),
        BEFriend("Jonas", "1234", false, "48 Cedar St."),
        BEFriend("Frederik", "12345", true, "58 Wall Road"),
        BEFriend("Mantas", "12345678", true, "5 Second St."),
        BEFriend("Michael", "23456789", true, "9975 Oak Valley St."),
        BEFriend("Jens", "87654321", false, "2 Maiden St."),
        BEFriend("Jan", "12121212", true, "43 Cherry Street")
    )

    fun getAll(): Array<BEFriend> = mFriends


    fun getAllNames(): Array<String> = mFriends.map { p -> p.name }.toTypedArray()

    fun getFavorites(): Array<BEFriend> = mFriends.filter { beFriend -> beFriend.isFavorite }.toTypedArray()


}
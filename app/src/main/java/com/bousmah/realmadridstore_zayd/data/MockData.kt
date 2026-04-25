package com.bousmah.realmadridstore_zayd.data

object MockData {
    val products = listOf(
        Product(
            id = "1",
            name = "Home Jersey 25/26",
            price = 90.0,
            category = "Jerseys",
            imageUrl = "https://assets.adidas.com/images/h_2000,f_auto,q_auto,fl_lossy,c_fill,g_auto/09e87dc4f83e454284ef598ea3773d9d_9366/Real_Madrid_25-26_Home_Jersey_White_JJ1931_21_model.jpg",
            storeUrl = "https://www.adidas.com/us/real-madrid-25-26-home-jersey/JJ1931.html"
        ),
        Product(
            id = "2",
            name = "Away Jersey 25/26",
            price = 90.0,
            category = "Jerseys",
            imageUrl = "https://assets.adidas.com/images/h_2000,f_auto,q_auto,fl_lossy,c_fill,g_auto/69b7b03005ed474e95c1bae97618e097_9366/Real_Madrid_25-26_Away_Jersey_Blue_JJ4182_21_model.jpg",
            storeUrl = "https://www.adidas.com/us/real-madrid-25-26-away-jersey/JJ4182.html"
        ),
        Product(
            id = "3",
            name = "Third Jersey 25/26",
            price = 90.0,
            category = "Jerseys",
            imageUrl = "https://assets.adidas.com/images/h_2000,f_auto,q_auto,fl_lossy,c_fill,g_auto/7355abe9c0ce4ab9a6ae6b448cd03b15_9366/Real_Madrid_25-26_Third_Jersey_Blue_JV5845_21_model.jpg",
            storeUrl = "https://us.shop.realmadrid.com/collections/jerseys-kits-third"
        ),
        Product(
            id = "4",
            name = "Kids Home Jersey",
            price = 70.0,
            category = "Kids",
            imageUrl = "https://assets.adidas.com/images/h_2000,f_auto,q_auto,fl_lossy,c_fill,g_auto/279a309bfe484183ac7cc90f4f1b6a1a_9366/Real_Madrid_25-26_Home_Jersey_Kids_White_JN8887_21_model.jpg",
            storeUrl = "https://www.adidas.com/us/real-madrid-25-26-home-jersey-kids/JN8887.html"
        ),
        Product(
            id = "5",
            name = "Training Kit",
            price = 65.0,
            category = "Training",
            imageUrl = "https://us.shop.realmadrid.com/cdn/shop/files/over-training.png?v=1767987071&width=2000",
            storeUrl = "https://us.shop.realmadrid.com/collections/adidas-training"
        ),
        Product(
            id = "6",
            name = "Scarf",
            price = 25.0,
            category = "Accessories",
            imageUrl = "https://shop.realmadrid.com/cdn/shop/files/image_83487ac0-4429-496e-816e-147598481ab2.png?v=1767814971&width=2000",
            storeUrl = "https://us.shop.realmadrid.com/collections/accessories"
        ),
        Product(
            id = "7",
            name = "Jacket",
            price = 110.0,
            category = "Training",
            imageUrl = "https://static.independent.co.uk/2023/11/11/15/SEI179371282.jpg",
            storeUrl = "https://us.shop.realmadrid.com/collections/training-jackets-mens"
        )
    )

    val stores = listOf(
        Store("Santiago Bernabéu Store", "C. de Concha Espina, 1, 28036 Madrid, Spain", "10:00 - 21:00", 40.453054, -3.688344),
        Store("Gran Vía Store", "C. de la Gran Vía, 31, 28013 Madrid, Spain", "10:00 - 22:00", 40.4203, -3.70379),
        Store("New York Official Store", "123 5th Ave, New York, NY 10003, USA", "09:00 - 20:00", 40.7392, -73.9903),
        Store("Dubai Mall Store", "Financial Center Rd, Dubai, UAE", "10:00 - 00:00", 25.1972, 55.2797),
        Store("Tokyo Shibuya Store", "Shibuya City, Tokyo, Japan", "11:00 - 21:00", 35.6580, 139.7016)
    )
}

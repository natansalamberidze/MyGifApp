# Жестко защищаем сам интерфейс GiphyApi от любого переименования
-keep interface com.example.network.api.GiphyApi { *; }

# Жестко защищаем класс ответа сервера со всеми его внутренними полями
-keep class com.example.network.model.GiphyResponse { *; }
-keep class com.example.network.model.** { *; }

# Защита внутренних механизмов Retrofit
-keep class retrofit2.** { *; }
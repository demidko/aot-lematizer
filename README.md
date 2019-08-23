## aot-lematizer -- Библиотека морфологического анализа на Java

### Решаемые задачи:
* Лемматизация (получение нормальной формы слова)
* Получение всех вариантов каждой леммы
* Получение грамматической информации для слова (часть речи, падеж, спряжение и т.д.)

### Особенности реализации:
* Ввиду частого взаимозаменяемого использования в речи,  
  буква ё рассматривается наравне с буквой е.

### Полезные ссылки:
* [Аббревиатуры использованые в перечислении GrammarInfo](http://phpmorphy.sourceforge.net/dokuwiki/manual-graminfo)  
* [Описание исходного формата словаря](https://sourceforge.net/p/seman/svn/HEAD/tree/trunk/Docs/Morph_UNIX.txt)
* [Консольное приложение использующее aot-lemmatizer](https://github.com/demidko/aot-lematizer/blob/master/testapp/src/main/java/com/farpost/aot/TestApplication.java)

### TODO:
1. Согласовать процесс компиляции:  
   редактируемая база данных -> mrd формат -> бинарный формат.

### Бинарный формат 
```
строковый fst:
  строка -> 
    id мета-леммы

лемма fst:
  id мета-леммы -> 
    индекс смещения для леммы 1
    id мета-леммы для строки 1, 
    индекс морфологии 1, 
    id мета-леммы для строки 2, 
    индекс морфологии 2, 
    .... 
    индекс смещения для леммы 2
    ...
    и так далее

массив морфологии
```
(В FST можно получить значение по ключу и ключ по значению)

  
  

# Pascal-Cplus

SETUP
1. git clone https://github.com/reFreshD2/Pascal-Cplus.git
2. Open Project in ApacheNetBeans
3. Run Project
4. PROFIT!

src/LexAnalyzer - класс лексического анализатора Pascal
  + LexAnalyzer(String fileName) - конструктор создания класса с указанием названия считываемого файла
  + void makeAnalysis() - метод формирования списка лексем из считываемого файла
  + void print() - метод вывода списа лексем на экран
  
src/Pair - класс лексемы - пара <name,value>
  + Pair(String t, String n) - конструктор лексемы
  + void print() - метод вывода лексемы на экран
  
src/Util - класс приложения транслятора Pascal - C++

Пример считывываемого файла program.txt находится в корне проекта

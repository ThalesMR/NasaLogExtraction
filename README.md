# NasaLogExtraction

1)extrair o projeto
2)entrar na pasta do projeto
3)compilar o projeto usando o sbt , ex: "sbt publish-local" ou "sbt package"
4)Pegar o jar gerado pelo pablish-local e colocar na pasta "~/" da maquina com spark
5)usar o seguinte comando no terminal linux para rodar o job "spark-submit --class br.com.nasa.run.NasaLogJob --master yarn http_nasa_thales_2.10.jar /path/to/folder/where"
6)*não esquecer que o projeto espera que exista uma pasta chamada "log" dentro do path "/path/to/folder/where", ou seja "/path/to/folder/where/log" onde os arquivos de log da nasa devem estar
7)Após rodar o job, o mesmo gera um arquivo "~/respostas.txt" contendo as respostas


Job visa responder as seguintes perguntas:
HTTP requests to the NASA Kennedy Space Center WWW server
Esses dois conjuntos de dados possuem todas as requisições HTTP para o servidor da NASA Kennedy
Space Center WWW na Flórida para um período específico

Os logs estão em arquivos ASCII com uma linha por requisição com as seguintes colunas:

● Host fazendo a requisição​. Um hostname quando possível, caso contrário o endereço de internet se o nome
não puder ser identificado.
● Timestamp ​no formato "DIA/MÊS/ANO HH:MM:SS YYYY"
● Requisição (entre aspas)
● Código do retorno HTTP
● Total de bytes retornados
Questões
Responda as seguintes questões devem ser desenvolvidas em Spark utilizando a sua linguagem de preferência.
1. Número de hosts únicos.
2. O total de erros 404.
3. Os 5 URLs que mais causaram erro 404.
4. Quantidade de erros 404 por dia.
5. O total de bytes retornados.


Qual o objetivo do comando cache ​em Spark?
Persistir em memória todas as transformações feita ao seu conjunto de dados , de forma que esse dado não precise ser recriado e passar por todas as transformações anteriores ao momento.

O mesmo código implementado em Spark é normalmente mais rápido que a implementação equivalente em
MapReduce. Por quê?
Spark processa o dado em memória, dessa forma não há disperdicio de tempo com latência de disco. Já o MapReduce armazena seu dado em disco, que possui uma latência maior.

Qual é a função do SparkContext?
Fazer a  "interface" entre seu Job e o Cluster Spark, definindo diversas informações de como seu programa deve rodar, onde acessar recursos.

Explique com suas palavras o que é Resilient Distributed Datasets​ (RDD).
Uma forma do Spark de representar um cojunto de dados. Sendo esses dados distribuidos,  imutáveis, tolerantes a falha e que podem ser processados de forma paralela.

GroupByKey ​é menos eficiente que reduceByKey ​em grandes dataset. Por quê?
No ReduceByKey o Spark sabe que pode combinar as saidas de mesma chave antes de trocar os dados com outras partições, diminuindo assim a quantidade de informações a serem trocas entre partes do cluster.
Já no GroupByKey será feita a transferencia de toda a informação de mesma chave para a mesma maquina no cluster e depois será aplicada a função para reduzir o dado.

Explique o que o código Scala abaixo faz.
```
val textFile = sc.textFile("hdfs://...")
val counts = textFile.flatMap(line => line.split(" "))
.map(word => (word, 1))
.reduceByKey(_ + _)
counts.saveAsTextFile("hdfs://...")
```
Basicamente lê um(ns) arquivo(s), para criar um conjunto de dados, divide esse conjunto de dados em palavras, conta quantas vezes essas palavras aparecem e depois grava em um arquivo texto esse conjunto de dados no formato (String, Int), onde a chave é a palavra e a saída é a quantidade de vezes que essa palavra aparece.



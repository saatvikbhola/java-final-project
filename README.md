# Java Project for 3rd Semester Finals
this projects mimics the working of a university management system where you can use admin and student functions with the implementation of database using the jdbc driver

## Structure of the File

<ul>
  <li>java-final-project
    <ul>
      <li>database
        <ul>
          <li>DatabaseManager.java</li>
        </ul>
      </li>
      <li>model
        <ul>
          <li>Course.java</li>
          <li>Student.java</li>
        </ul>
      </li>
      <li>main
        <ul>
          <li>Main.java</li>
        </ul>
      </li>
    </ul>
  </li>
</ul>



## How to run on terminal 

Use this line for compiling 
`javac -cp ".:path/to/mysql-connector-java-x.x.xx.jar" database/DatabaseManager model/Course.java model/Student.java main/Main.java`

Use this for executing 
`java -cp ".:path/to/mysql-connector-java-x.x.xx.jar" main.Main`

> [!IMPORTANT]
> change "path/to/mysql-connector-java-x.x.xx.jar" to the actual path of the jdbc connector on your system when running.
> change the 'jdbc_url', 'user', 'password' acording to your database



<?php
/**
 * Created by PhpStorm.
 * User: Alamin Mahamud
 * Date: 5/23/2015
 * Time: 10:41 PM
 */

class DB_Connect{

    // Constructor
    function __construct(){

    }
    // Destructor
    function __destruct(){
        // $this->close();
    }

    // Connecting TO Database
    public function connect(){
        require_once 'include/config.php';

        // connecting to mysql
        $con = mysql_connect(DB_HOST, DB_USER,DB_PASS);

        // Selecting Database
        mysql_select_db(DB_DATABASE, $con);

        // returns database handler
        return $con;
    }

    // Closing Database Connection
    public function close(){
        mysql_close();
    }
}

?>

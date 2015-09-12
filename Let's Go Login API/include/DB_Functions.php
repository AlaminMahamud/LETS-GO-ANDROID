<?php
/**
 * Created by PhpStorm.
 * User: Ashraf uddin
 * Date: 5/30/2015
 * Time: 9:49 AM
 */

class DB_Functions {

    /**
     *
     * 01. Database Class
     * 02.
     *
     * @var
     */

    private $db;
    // Constructor
    function __construct(){
        require_once 'DB_Connect.php';
        // Connecting TO Database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
    // Destructor
    function __destruct(){
    }

    function randomPassword(){
        $alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*(){}[]/?><.,";
        $pass = array();
        $alphaLength = strlen($alphabet) - 1;
        for($i=0; $i<32;$i++){
            $n = rand(0,$alphaLength);
            $pass[] = $alphabet[$n];
        }
        return implode($pass);
    }

    public function forgotPassword($email, $newPassword){
        $sql="UPDATE users SET password='$newPassword' WHERE email='$email';";
        $result = mysql_query($sql);
        if($result){
            return true;
        }else{
            return false;
        }

    }

    public function updateGpsData($phone, $lat, $long){
        $sql="UPDATE users SET lat='$lat', lng = '$long' WHERE phone = '$phone' ";
        $result = mysql_query($sql);
        if($result){
            return true;
        }else{
            return false;
        }

    }

    public function storeUser($name,$uname, $phone, $email, $password){
        $sql = "INSERT INTO users(name, username, email, phone, password) VALUES ('$name', '$uname','$email', '$phone', '$password')";
        $result = mysql_query($sql);
        // check for successful store
        if($result){
            // get User Details
            $uid = mysql_insert_id(); // last Inserted iD
            $sql2="SELECT*FROM users WHERE id = '$uid'  ";
            $result = mysql_query($sql2);
            //return user details
            return mysql_fetch_array($result);
        }else{
            return false;
        }
    }

    public function getUserByPhoneAndPassword($phone, $password){
        $sql = "SELECT * FROM users WHERE phone = '$phone'";
        $result = mysql_query($sql) or die(mysql_error());
        // check for result
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows>0){
            $result = mysql_fetch_array($result);
            $pass = $result['password'];
            // Check for Password Equality
            if($password == $pass){
                // User authientication details are correct
                return $result;
            }else{
                // user not found
                return false;
            }
        }
    }


    /**
     * Check user is existed or not
     */
    public function isUserExisted($phone) {
        $result = mysql_query("SELECT * FROM users WHERE phone = '$phone'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
    // user existed
            return true;
        } else {
    // user not existed
            return false;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExistedByEmail($email) {
        $result = mysql_query("SELECT * FROM users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
    // user existed
            return true;
        } else {
    // user not existed
            return false;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExistedByUsername($username) {
        $result = mysql_query("SELECT * FROM users WHERE username = '$username'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
    // user existed
            return true;
        } else {
    // user not existed
            return false;
        }
    }

	
	/***
	GET USER 
	***/
	
	public function getAllUsers(){
		$result =  mysql_query("SELECT * FROM users ");
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
	    // user existed
            return mysql_fetch_array($result);
        } else {
    // user not existed
            return false;
        }
		
	}

    /**
     * Search User By phone & return the result
     *
     */
    public function searchUserByPhone($phone){
        $sql = "SELECT * FROM `users` WHERE phone = '$phone' ";
        $result= mysql_query($sql);
        $no_of_rows = mysql_num_rows($result);
        if($no_of_rows > 0){
            // User Existed
            return mysql_fetch_array($result);
        }else{
            // user not Exist
            return false;
        }
    }

   /***
    Get Gps Data By Phone
    ***/
    
    public function getGPSData($phone){
	    	$sql = "SELECT * FROM users WHERE phone = '$phone' ";
	        $result= mysql_query($sql);
	        $no_of_rows = mysql_num_rows($result);
	        if($no_of_rows > 0){
	            // User Existed
	            return mysql_fetch_array($result);
	        }else{
	            // user not Exist
	            return false;
	        }
    	}
} 
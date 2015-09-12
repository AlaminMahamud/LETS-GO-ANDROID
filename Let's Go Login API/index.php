<?php
/**
 * Created by PhpStorm.
 * User: Ashraf uddin
 * Date: 5/30/2015
 * Time: 10:34 AM
 */



/**
 * 01. login
 * 02. register
 * 03. changePassword
 * 04. forgetPassword
 * 05. updateGPSData
 * 06. getContactByPhone
 * 07. getLatitudeAndLongitude
 * 08. getContacts
 *
 *
 *
 *
 */

// TAG NAMES

if(isset($_POST['tag']) && $_POST['tag']!=''){
    // get A Tag
    $tag = $_POST['tag'];

    // Include Database handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

    // response array
    $response = array("tag"=>$tag, "success"=>0, "error"=>0);

    // 01. Login
    if($tag == 'login'){
        // Request Type is Check Login
        $phone = $_POST['phone'];
        $password = $_POST['password'];

        // Check For User
        $user = $db->getUserByPhoneAndPassword($phone,$password);
        if($user!=false){
            // user found
            // echo json with Success

            $response["success"]=1;
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["username"] = $user["username"];
            $response["user"]["phone"] = $user["phone"];
            echo json_encode($response);
        }else{
            // user not found
            // echo json with Error
            $response["error"]=1;
            $response["error_msg"]="Incorrect Phone or password!";
            echo json_encode($response);
        }
    //02. Register
    }elseif($tag == 'register'){
            // Request Type is new User
            $name = $_POST['name'];
            $email = $_POST['email'];
            $username = $_POST['username'];
            $phone = $_POST['phone'];
            $password = $_POST['password'];

            $subject = "Registration";
            $message = "Hello $name.\n\nYou Have SuccessFully Registered into our Service.\n\nRegards,\nIneedAHelp.com Team";
            $from    = "support@ineedahelp.com";
            $headers = "From: ".$from;

            // Check User is Already Existed
            if($db->isUserExisted($phone) && isUserExistedByEmail($email) && isUserExistedByUsername($username)){
                // User is Already existed - error Response
                $response["error"]=2;
                $response["error_msg"]="User Already Existed";
                echo json_encode($response);
            }else{
                // Store User
                $user = $db->storeUser($name,$username, $phone, $email, $password);
                if($user){
                    // User Stored Successfully
                    $response["success"]=1;
                    $response["user"]["name"] = $user["name"];
                    $response["user"]["email"] = $user["email"];
                    $response["user"]["username"] = $user["username"];
                    $response["user"]["phone"]   = $user["phone"];

                    mail($email, $subject, $message, $headers);
                    echo json_encode($response);


                } else{

                    // User failed To Register
                    $response["error"] = 1;
                    $response["error_msg"] = "JSON Error Occurred in Registration";
                    echo json_encode($response);
                }
            }
        }elseif($tag == 'chgpass'){
                $email = $_POST["email"];
                $newpassword = $_POST["newpas"];
                $subject = "Change Password Notification";
                $message = "Hello User, \n\nYour Password is successfully Changed. \n\nRegards, \nINeedAHelp Team";
                $from = "support@ineedahelp.com";
                $headers = "From:". $from;

                if($db->isUserExisted($phone)){
                $user = $db->forgotPassword($phone, $newpassword);
                if($user){
                    $response["success"]=1;
                    mail($email, $subject, $message, $headers);
                    echo json_encode($response);
                }else{
                    $response["error"]=1;
                    echo json_encode($response);
                }
                }else{
                    // User is Already existed - error Response
                    $response["error"]=2;
                    $response["error_msg"]="User Not Exists";
                    echo json_encode($response);
                    }
    }elseif($tag=='forpass'){
        $forgotpassword_email = $_POST['email'];
        $randomcode = $db->randomPassword();
        $subject = "Password Recovery";
        $message = "Hello User \n\nYour new password is successfully changed.\nYour new Password is $randomcode \nLogin With your new password and change it in the user panel.\n\n Regards,\nIneedAHelp.com Team";
        $from = "support@ineedahelp.com";
        $headers = "From:" . $from;
        if($db->isUserExisted($forgotpassword)){
            $user = $db->forgotPassword($forgotpassword, $randomcode);
            if ($user) {
                $response["success"] = 1;
                mail($forgotpassword,$subject,$message,$headers);
                echo json_encode($response);
            }
            else {
                $response["error"] = 1;
                echo json_encode($response);
            }
        }else{

            // User not Existed- error Response
            $response["error"] = 2;
            $response["error_msg"]="User not Exist";
            echo json_encode($response);
        }


    }elseif($tag=='updateGPSData'){
        $phone = $_POST['phone'];
        $lat   = $_POST['lat'];
        $long  = $_POST['longi'];

        // update gps data
        $user = $db->updateGpsData($phone, $lat, $long);
        if($user){
            //Updated Successfully
            $response["success"]=1;
            echo json_encode($response);
        }else{
            $response["success"]=0;
            echo json_encode($response);
        }
    }elseif($tag == 'getContactByPhone'){
        $phone = $_POST['phone'];
        // Check for User
        $user = $db->searchUserByPhone($phone);
        if($user!=false){
            // user found
            // echo JSON with Success
            $response["success"]=1;
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["username"] = $user["username"];
            $response["user"]["phone"] = $user["phone"];
            $response["user"]["lat"] = $user["lat"];
            $response["user"]["longi"] = $user["lng"];
            echo json_encode($response);
        }else{
            // User not found
            // echo JSON with error
            $response["error"]=1;
            echo json_encode($response);
        }

    }else if($tag == 'getContacts'){
while($user = $db-> getAllUsers()){
	
             $res["use"]["username"] = $user["username"];
            $res["use"]["latitude"] = $user["lat"];
            $res["use"]["longitude"] = $user["lng"];
            $response["user"][]=$res;
        
	}
	 $response["success"]=1;
        echo json_encode($response);
	   
    }else if($tag == 'getGpsData'){
        $phone = $_POST['phone'];
        // Check for User
        $user = $db->getGpsData($phone);
        if($user!=false){
            // user found
            // echo JSON with Success
            $response["success"]=1;
            $response["user"]["username"] = $user["username"];
            $response["user"]["lat"] = $user["lat"];
            $response["user"]["longi"] = $user["lng"];
            echo json_encode($response);
        }else{
            // User not found
            // echo JSON with error
            $response["error"]=1;
            echo json_encode($response);
        }

    }

}else{
    echo "I NeedAHelp Login API";
}
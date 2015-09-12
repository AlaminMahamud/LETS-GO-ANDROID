
<?php

mysql_connect("ineedahelp.com","ineedahe_alamin","!)!!#(%$(")or die("could not connect to mysql");
mysql_select_db("ineedahe_android")or die("no database");

$phone =$_POST['phone'];
$lat = $_POST['lat'];
$long =$_POST['longi'];
$count=false;
$q = "UPDATE users SET lat =  '".$lat."',lng =  '".$long."' WHERE phone =  '".$phone."'";
$count = mysql_query($q) or die($query."<br/><br/>".mysql_error());
if($count)
{
    $response["latitude"]=$lat;
    $response["longitude"]=$long;
    $response["success"]=1;
 }

else
{
    $response["success"]=0;
} 
echo json_encode($response);




?>


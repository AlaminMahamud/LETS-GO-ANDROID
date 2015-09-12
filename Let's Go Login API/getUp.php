<?php
mysql_connect("ineedahelp.com","ineedahe_alamin","!)!!#(%$(")or die("could not connect to mysql");
mysql_select_db("ineedahe_android")or die("no database");
$result = mysql_query("Select * from  users "); 
$count = mysql_num_rows($result);
$response = array();
if($count>0)
{
 $response["success"] = 1;
 $r=array();
 while($row = mysql_fetch_array($result))
 {
    $r["username"] = $row['username'];
    $r["lat"]= $row['lat'];
    $r["lng"] = $row['lng'];
    $response["user"][] = $r;
 }
}
else
{

 $response["success"] = 0;
} 
echo json_encode($response);
?>
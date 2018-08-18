
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    </head>
    <body>
        <div>
            <img id="img_capcha" alt="img" src=""/>
            <input id='get_capcha' type="button" value="getimg" />
        </div>
        <h1>Form send text capcha</h1>
        <form id="form_send_capcha" action="setCap" method="POST">
            <input type="text" name="captext" value="" size="300" />
            <input type="submit" value="send" name="send" />
        </form>
        <s:url value="/getCapTypeBase64" var="getCapTypeImg" scope="request"/>
        <script>
            function getImgCapcha(input) {
                $.ajax({
                    type: "GET",
                    url: input,
                    timeout: 100000,
                    success: function (data) {
                        console.log("SUCCESS: ", data);
                        displayImg(data);

                    },
                    error: function (e) {
                        console.log("ERROR: ", e);
                        display(e);
                    }
                });
            }
            $().ready(function (){
                $('#img_capcha').css('display','none');
            })
            
            $().ready(function (){
                $('#get_capcha').click(function (){
                    getImgCapcha("${getCapTypeImg}");
                });
            });
            function displayImg(data){
                $('#img_capcha').attr('src',"data:image/png;base64, "+data+"");
                $('#img_capcha').css('display','block');
            }
        </script>
    </body>
</html>

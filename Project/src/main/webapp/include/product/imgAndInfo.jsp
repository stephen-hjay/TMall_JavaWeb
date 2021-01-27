

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
 
<script>
 
$(function(){
    var stock = ${p.stock};
    $(".productNumberSetting").keyup(function(){
        var num= $(".productNumberSetting").val();
        num = parseInt(num);
        if(isNaN(num))
            num= 1;
        if(num<=0)
            num = 1;
        if(num>stock)
            num = stock;
        $(".productNumberSetting").val(num);
    });
     
    $(".increaseNumber").click(function(){
        var num= $(".productNumberSetting").val();
        num++;
        if(num>stock)
            num = stock;
        $(".productNumberSetting").val(num);
    });
    $(".decreaseNumber").click(function(){
        var num= $(".productNumberSetting").val();
        --num;
        if(num<=0)
            num=1;
        $(".productNumberSetting").val(num);
    });
     
    $(".addCartButton").removeAttr("disabled");

    // ajax add to cart
    $(".addCartLink").click(function(){
        var page = "forecheckLogin";
        $.get(
                page,
                function(result){
                    if("success"==result){
                        var pid = ${p.id};
                        var num= $(".productNumberSetting").val();
                        var addCartpage = "foreaddCart";
                        $.get(
                                addCartpage,
                                {"pid":pid,"num":num},
                                function(result){
                                    if("success"==result){
                                        $(".addCartButton").html("Added to Shopping Cart");
                                        // $(".addCartButton").attr("disabled","disabled");
                                        // $(".addCartButton").css("background-color","lightgray");
                                        // $(".addCartButton").css("border-color","lightgray");
                                        // $(".addCartButton").css("color","black");
                                        var cartNum= $("#shoppingCart").val()+num;
                                        $("#shoppingCart").html(cartNum+"");
                                    }
                                    else{
                                        $(".addCartButton").html("Failed, Try Again");
                                    }
                                }
                        );                          
                    }
                    else{
                        $("#loginModal").modal('show');                     
                    }
                }
        );      
        return false;
    });
    // check if login
    $(".buyLink").click(function(){
        var page = "forecheckLogin";
        $.get(
                page,
                function(result){
                    if("success"==result){
                        // 获取 购买件数
                        var num = $(".productNumberSetting").val();
                        // 组成 url 进行跳转
                        location.href= $(".buyLink").attr("href")+"&num="+num;
                    }
                    else{
                        $("#loginModal").modal('show');                     
                    }
                }
        );      
        return false;
    });

    // modal jsp 的错误信息 提示页面
    // $("button.loginSubmitButton").click(function(){
    //     var name = $("#name").val();
    //     var password = $("#password").val();
    //
    //     if(0==name.length||0==password.length){
    //         $("span.errorMessage").html("Please enter your username and password");
    //         $("div.loginErrorMessageDiv").show();
    //         return false;
    //     }
    //
    //     var page = "foreloginAjax";
    //     $.get(
    //             page,
    //             {"name":name,"password":password},
    //             function(result){
    //                 if("success"==result){
    //                     location.reload();
    //                 }
    //                 else{
    //                     $("span.errorMessage").html("Invalid User Name or Password!");
    //                     $("div.loginErrorMessageDiv").show();
    //                 }
    //             }
    //     );
    //
    //     return true;
    // });
    //


   // modal jsp 的错误信息 提示页面
    $("button.loginSubmitButton").click(function(){
        var name = $("#name").val();
        var password = $("#password").val();

        if(0==name.length||0==password.length){
            $("span.errorMessage").html("Please enter your username and password");
            $("div.loginErrorMessageDiv").show();
            return false;
        }

        var page = "foreloginAjax";
        $.post(
                page,
                 // 这样去带参数
                {"name":name,"password":password},
                function(result){
                    if("success"==result){
                        location.reload();
                    }
                    else{
                        $("span.errorMessage").html("Invalid User Name or Password!");
                        $("div.loginErrorMessageDiv").show();
                    }
                }
        );

        return true;
    });
    $("img.smallImage").mouseenter(function(){
        var bigImageURL = $(this).attr("bigImageURL");
        $("img.bigImg").attr("src",bigImageURL);
    });
     
    // $("img.bigImg").load(
    //     function(){
    //         $("img.smallImage").each(function(){
    //             var bigImageURL = $(this).attr("bigImageURL");
    //             img = new Image();
    //             img.src = bigImageURL;
    //
    //             img.onload = function(){
    //                 $("div.img4load").append($(img));
    //             };
    //         });
    //     }
    // );

    $("img.bigImg").load(function(){
        // 判断是否需要预加载
        if(0 == $("div.img4load").children().length){
            console.info("预加载：" + $("div.img4load").children().length);
            $("img.smallImage").each(function(){
                var bigImageURL = $(this).attr("bigImageURL");
                img = new Image();
                img.src = bigImageURL;

                img.onload = function(){
                    console.log(bigImageURL);
                    $("div.img4load").append($(img));
                }
            });
        }
    });
});
 
</script>
 
<div class="imgAndInfo">
 
    <div class="imgInimgAndInfo">
        <%-- 默认显示第一张图片 --%>
        <img src="img/productSingle/${p.firstProductImage.id}.jpg" class="bigImg">
            <%--   5张小图片    --%>
            <div class="smallImageDiv">
            <c:forEach items="${p.productSingleImages}" var="pi">
                <img src="img/productSingle_small/${pi.id}.jpg" bigImageURL="img/productSingle/${pi.id}.jpg" class="smallImage">
            </c:forEach>
            </div>

        <div class="img4load hidden" ></div>
    </div>
     
    <div class="infoInimgAndInfo">
        <%--    商品 小标题 和 标题     --%>
        <div class="productTitle">
            ${p.name}
        </div>
        <div class="productSubTitle">
            ${p.subTitle} 
        </div>
         
        <div class="productPrice">
            <div class="juhuasuan">
                <span class="juhuasuanBig" >聚划算</span>
                <span>此商品即将参加聚划算，<span class="juhuasuanTime">1天19小时</span>后开始，</span>
            </div>
            <div class="productPriceDiv">
                <div class="gouwujuanDiv"><img height="16px" src="img/site/gouwujuan.png">
                <span> 全天猫实物商品通用</span>
                 
                </div>
                <div class="originalDiv">
                    <span class="originalPriceDesc">Price</span>
                    <span class="originalPriceYuan">$</span>
                    <span class="originalPrice">
                     
                        <fmt:formatNumber type="number" value="${p.orignalPrice}" minFractionDigits="2"/>                 
                    </span>
                </div>
                <div class="promotionDiv">
                    <span class="promotionPriceDesc">Promotion Price </span>
                    <span class="promotionPriceYuan">$</span>
                    <span class="promotionPrice">
                        <fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2"/>
                    </span>               
                </div>
            </div>
        </div>
        <div class="productSaleAndReviewNumber">
            <div>Sales <span class="redColor boldWord"> ${p.saleCount }</span></div>
            <div>Cumulative Reviews <span class="redColor boldWord"> ${p.reviewCount}</span></div>
        </div>
        <div class="productNumber">
            <span>Count</span>
<%--            --%>
            <span>
                <span class="productNumberSettingSpan">
                <input class="productNumberSetting" type="text" value="1">
                </span>
                <span class="arrow">
                    <a href="#nowhere" class="increaseNumber">
                    <span class="updown">
                            <img src="img/site/increase.png">
                    </span>
                    </a>
                     
                    <span class="updownMiddle"> </span>
                    <a href="#nowhere"  class="decreaseNumber">
                    <span class="updown">
                            <img src="img/site/decrease.png">
                    </span>
                    </a>
                     
                </span>
                     
            件</span>
            <span>In-Stock: ${p.stock}件</span>
        </div>
        <div class="serviceCommitment">
            <span class="serviceCommitmentDesc">Service Promise</span>
            <span class="serviceCommitmentLink">
                <a href="#nowhere">Guarantee</a>
                <a href="#nowhere">Flash Refund</a>
                <a href="#nowhere">Delivery Insurance</a>
                <a href="#nowhere">Unconditional Refund or Replace</a>
            </span>
        </div>    
         
        <div class="buyDiv">
            <a class="buyLink" href="forebuyone?pid=${p.id}"><button class="buyButton">Buy It Now</button></a>
            <a href="#nowhere" class="addCartLink"><button class="addCartButton"><span class="glyphicon glyphicon-shopping-cart"></span>Add to cart</button></a>
        </div>
    </div>
     
    <div style="clear:both"></div>
     
</div>
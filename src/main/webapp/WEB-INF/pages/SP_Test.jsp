<%
  /*****************************************************************************
   * Project7 주민번호 제거 프로젝트
   *****************************************************************************
   * 1.FILE NAME      : SP_Test.jsp
   * 2.MENU LOCATION  : SP 테스트
   * 3.PROCESS        :
   * 4.작 성 자        : 노영선
   * 5.작 성 일 자     :
   * 6.수 정 일 자     :
   * . <날짜>      : <수정 내용> (<개발자명>)
   * . 2015-05-23 : 신규개발        ( 노영선 )
   * 설정) db.properties DB로그인 계정이 DBAUSER 이여야 합니다.
   *****************************************************************************/

%>

<%--
  Created by IntelliJ IDEA.
  User: youngsunkr
  Date: 2015-05-23
  Time: 오전 7:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="Content-Style-Type" content="text/css">

  <title>SP 테스트 화면</title>
  <style type="text/css">
    td {
      font-family:Verdana;
      font-size:12px;
      color:#67645C;
      text-align:center;
    }
    body { margin:0px; padding:0px; }
    br{
      line-height:10px
    }
    .bottom{
      background-color: #CEC8B9;
      vertical-align: middle;
      font-weight: bold;
      text-align:center;
    }
    .aa{
      background-color: #F1EFEB;
      font-weight: bold;
      text-align:right;
    }
    .bb{
      background-color: #E0DDD3;
      font-weight: bold;
      text-align:center;
      vertical-align: middle;
    }
    .httpf{
      background-color: #F1EFEB;
      border:1 1 1 1;
      border-style:solid;
      border-color:#F1EFEB
    }
    li.resultCode {
      width:50%;
      font-size:12;
      display:inline;
      vertical-align:middle;
      border:1 solid #c3c3c3}

    li.resultRes {
      width:49.8%;
      font-size:12;
      vertical-align:middle;
      display:inline;
      border:1 solid #c3c3c3}

    .semove {
      width:85%
    }
    .insemove {
      width:85%
    }
    /* listA */
    table.list {
      width: 1000px;
      table-layout: auto;
      width: auto;
    }
    table.list th {
      white-space: nowrap;
      word-break:break-all;
      color: #666666;
      padding:5px 0px 4px 0px;
      border-left: 1px solid #000000;
      border-right: 1px solid #000000;
      border-bottom: 1px solid #000000;
      border-top: 1px solid #000000;
      background: #E0DDD3;
    }
    table.list tr {
      white-space: nowrap;
      color: #666666;
      padding:5px 0px 4px 0px;
      border-bottom: 1px solid #C3D0DC;
      border-right: 1px solid #C3D0DC;
      background: #E2EBF4;
    }

    table.list td {
      white-space: nowrap;
      word-break:break-all;
      text-align: center;
      padding: 4px 4px 3px 4px;
      border-left: 1px solid #C3D0DC;
      border-right: 1px solid #C3D0DC;
      border-bottom: 1px solid #C3D0DC;
      border-top: 1px solid #C3D0DC;
      background: #ffffff;
    }
  </style>

  <script src="<c:url value='/common/js/jquery1.4.4.js'/>" 			type="text/javascript"></script>
  <script src="<c:url value='/common/js/json.js'/>" 					type="text/javascript"></script>
  <script src="<c:url value='/common/js/jquery.form.js'/>" 			type="text/javascript"></script>
  <script src="<c:url value='/common/js/jquery.form.ex.js'/>" 		type="text/javascript"></script>
  <script src="<c:url value='/common/js/jquery.blockUI.js'/>" 	type="text/javascript"></script>

  <script language="JavaScript">

    //공통변수
    var arr="";
    var wd = new Array("일","월","화","수","목","금","토");
    var options = null;

    //ready
    $(document).ready(function(){

      //list 숨기기
      $('#list').hide();

      //call watch
      watch();
      setInterval('watch()',1000);

      //event KEY_UP
      $('#txt_spname').keyup(function(e){
        if(e.keyCode == 13){
          options = {
            url:           'sptestGetSpList.do',
            dataType:      'json',
            beforeSubmit:  showRequest,
            success:       showResponse
          };

          $.blockUI();
          $('#spForm').submit();
        }
      });

      $('#txt_xmlname').keyup(function(e){
        if(e.keyCode == 13){
          options = {
            url:           'sptestGetSpList.do',
            dataType:      'json',
            beforeSubmit:  showRequest,
            success:       showResponse
          };

          $.blockUI();
          $('#spForm').submit();
        }
      });

      //event ON CLICK
      $('#sel_splist').click(function(){
        //alert("click  :: " + $('#sel_splist').val());
        if($('#sel_splist').val()!=null){
          options = {
            url:           'sptestXmlReadArgu.do',
            dataType:      'json',
            beforeSubmit:  showRequest,
            success:       showResponse
          };

          $.blockUI();
          $('#spForm').submit();
        }
      });

      //testStart CLICK
      $('#btn_submit').click(function(){
        if($('#txt_xmlname').val()==""){
          alert("SP가 정의되어 있는 xml명을 입력하세요(확장자 없이)\nEx)Test.xml -> [Test] 입력");
          $('#txt_xmlname').focus();
          return;
        }

        if($('#sel_splist').val()==null){
          alert("테스트할 SP를 선택하여 주세요.");
          return;
        }

        options = {
          url:           'sptestSpTestStart.do',
          dataType:      'json',
          beforeSubmit:  showRequest,
          success:       showResponse
        };

        $.blockUI();
        $('#spForm').submit();
      });

      //readArgu CLICK
      $('#btn_readArgu').click(function(){
        if($('#sel_splist').val()==null){
          alert("읽어올 SP를 선택하여 주세요.");
          return;
        }

        options = {
          url:           'sptestReadArgu.do',
          dataType:      'json',
          beforeSubmit:  showRequest,
          success:       showResponse
        };

        $.blockUI();
        $('#spForm').submit();
      });

      //form submit
      $('#spForm').submit(function(){
        $(this).ajaxSubmit(options);
        return false;
      });

      //showlist CLICK
      $('#btn_showlist').click(function(){
        $('#mov').hide(500);
        $('#list').slideDown(500);
      });

      //hidelist CLICK
      $('#btn_hidelist').click(function(){
        $('#mov').slideDown(500);
        $('#list').slideUp(500);
      });
    });


    //show time
    function watch(){
      today = new Date();
      str = today.getFullYear() + "년 " + (today.getMonth()+1) + "월 " + today.getDate() + "일";
      str += "(" + wd[today.getDay()] + "요일) ";
      str += today.getHours() > 12 ? "오후 " + (today.getHours()-12) : "오전 " + today.getHours();
      str += "시 " + today.getMinutes() + "분 " + today.getSeconds() + "초";
      $('#txt_Time').val(str);
    }


    //request
    function showRequest(formData, jqForm) {

    }

    //respone
    function showResponse(responseText, statusText) {
      $.unblockUI();

      $('#erCode').val("");
      $('#erMessage').val("");

      switch(responseText.ResponseKey)
      {
        //sp목록 조회 성공
        case 'LIST_SUCCESS' :
          var options = '';
          $.each(responseText.SpList, function(key,value) {
            options += '<option value="' + responseText.SpList[key].object_id + '">['+ responseText.SpList[key].status +'] '+ responseText.SpList[key].object_name + '</option>';
          });
          $("#sel_splist").html(options);

          break;

        //ARGUMENTS 목록 조회 성공
        case 'AGLIST_SUCCESS' :
          //기존  ARGUMENTS text 제거
          $('.semove').remove();
          $('.insemove').remove();
          $('.codeText').remove();

          //ARGUMENTS text를 생성해 준다
          var cn=0;
          var temp="";
          var temp1="";
          $.each(responseText.AGList, function(key,value) {
            var cnt=0;
            if(cn<9)cnt="0"+(cn+1);
            else cnt=cn+1;

            $('#sText'+cn).after('<a class="codeText">'+cnt+'</a><input type="text" id="sText'+(cn+1)+'" readonly="readonly" class="semove" value="'+responseText.AGList[key]+'"/>');
            $('#ecode'+cn).after('<a class="codeText">'+cnt+'</a><input name="'+responseText.AGList[key]+'" class="insemove" type="text" id="ecode'+(cn+1)+'" value="" />');

            temp += responseText.AGList[key]+'\n';

            cn++;
          });

          $('#textarea1').val(temp);

          break;

        //ARGUMENTS 목록 조회 실패
        case 'AGLIST_FAIL' :
          $('.semove').remove();
          $('.insemove').remove();
          $('.codeText').remove();
          $('.resultCode').remove();
          $('.resultRes').remove();
          $('.list').remove();
          alert("유효한 SP가 아닙니다.\nARGUMENTS를 조회할수 없습니다람쥐.");

          break;

        // SP TEST 성공
        case 'SP_SUCCESS' :
          //응답속도
          $('#responseTime').val(responseText.totalTime);

          //result_str 셋팅
          $('.resultCode').remove();
          $('.resultRes').remove();
          $('.list').remove();
          $('#resultSetHeader').after(responseText.result_str);
          $('#show_list').html(responseText.result_list);

          $('#erCode').val(responseText.ot_respon_code);
          $('#erMessage').val(responseText.ot_res_msg);

          if(responseText.ot_respon_code == "00000"){
            alert("[정상적으로 종료되었습니다.]\n===========================\n\n코드 : " + responseText.ot_respon_code + "\n메세지 : " + responseText.ot_res_msg);
          }
          else{
            alert("[오류가 발생하였습니다.]\n===========================\n\n오류코드 : " + responseText.ot_respon_code + "\n메세지 : " + responseText.ot_res_msg);
          }
          break;

        // SP TEST 실패
        case 'SP_FAIL' :
          $('.resultCode').remove();
          $('.resultRes').remove();
          $('.list').remove();
          alert("SP 테스트 실행중 오류발생!!\n" + responseText.error_msg);

          break;

        // ARGUMENTS 파일읽기 성공
        case 'READ_SUCCESS' :
          var arguments=responseText.readargu.split(",");
          var text = "";
          for(var i=0;i<arguments.length;i=i+2){
            var h = arguments[i];
            var d = arguments[i+1];
            text = text + h + " = " + d +"\n";
            $('input[name = "'+h+'"]').val(d);
          }
          alert("Agruments !!\n" +text);

          break;
      }

    }

    // 카피
    function clipMessenger(id){
      var temp1 = $('#textarea1').val();
      if(temp1 == ""){
        alert("복사 할 수 없습니다.");
        return;
      }
      var idxs = document.getElementById(id);
      if(idxs.value==''){ document.body.focus(); return; }
      idxs.select();
      var clip=idxs.createTextRange();
      clip.execCommand('copy');

      alert('스크랩 되었습니다.\n 붙여넣기(Crrl+V)하여 주십시오.');
    }

    // 값 세팅
    function setValues(){
      var temp1 = $('#textarea1').val();
      var temp2 = $('#textarea2').val();
      var splitTemp1 = "";
      var sumTemp = "";

      if(temp1 == ""){
        alert("복사 할 수 없습니다.");
        return;
      }

      splitTemp1 = temp1.split("\n");

      for(var i=0; i < splitTemp1.length; i++){
        if(splitTemp1[i] == "") break;

        sumTemp += eval("document.spForm."+splitTemp1[i]).value+"\n"
      }

      $('#textarea2').val(sumTemp);

      clipMessenger('textarea2');
    }

    //결과값 초기화
    function setResultRemove(){
      $('.resultCode').remove();
      $('.resultRes').remove();
      $('#erCode').val("");
      $('#erMessage').val("");
    }
  </script>

</head>

<body>

<div id="mov" align="center" style="margin-top: 0px;">
  <form id="spForm" name="spForm" method="post" action="">
    <table width="1200" height="690" border="1" id="main">
      <tr>
        <td height="15" colspan="4" style="text-align: center;" class="aa">
          <div align="right">
            <input id="txt_Time" readonly="readonly" style="width:275px; background-color:#F1EFEB;margin-left:10px;border: 0 0 0 0;"/>
            (응답속도:<input id="responseTime" readonly="readonly" size="5" style="text-align: right;border: 0 0 0 0;background-color:#F1EFEB;"/>초)
            <br/>
          </div>
          <div align="left" style="padding-left: 10px">
            SP 명 : <input type="text" id="txt_spname" name="txt_spname" size="80" value=""/>
            XML 파일명 : <input type="text" id="txt_xmlname" name="txt_xmlname" size="30" value="SP_Test"/>
            AutoCommit : <input type="checkbox" id="chk_autocommit" name="chk_autocommit"/>
          </div>
        </td>
      </tr>
      <tr>
        <td width="283" height="21" class="bb">SP LISTS</td>
        <td width="178" class="bb">ARGUMENTS <input type="button" value="copy" onclick="clipMessenger('textarea1')"/></td>
        <td width="178" class="bb">INPUT VALUES <input type="button" value="copy" onclick="setValues();"/></td>
        <td class="bb">OUTPUT<input type="button" value="clear" onclick="setResultRemove();"/></td>
      </tr>
      <tr>
        <td height="241" align="center" valign="top" >
          <select id="sel_splist" name="sel_splist" size="32" style="width: 275px"></select>
        </td>
        <td align="center" valign="top">
          <div style="width:190px;height:500px; overflow-y:auto; padding:0px; border:3; border-style:solid; border-color:#EBEBEB">
            <input type="hidden" id="sText0"/>
          </div>
        </td>
        <td align="center" valign="top">
          <div style="width:190px;height:500px; overflow-y:auto; padding:0px; border:3; border-style:solid; border-color:#EBEBEB">
            <input  type="hidden"  id="ecode0"/>
          </div>
        </td>
        <td width="532" align="center" valign="top">
          <div id="result" style="overflow-y:auto;overflow-x:auto;width:100%;height:500px;" align="left" >
            <ul id="resultSetHeader" style="color: #FFF;margin:0 0 0 0;background-color: #999;border-bottom: 2 double #00F;text-align: center" >
              <li style="font-size: 15; font-family:Verdana;width: 50%;display: inline;" >CODE</li>
              <li style="font-size: 15;width: 49.8%;display: inline;" >RESULT</li>
            </ul>
          </div>
        </td>
      </tr>
      <tr>
        <td height="18">
          <input type="hidden"  />
        </td>
        <td>
          <input type="button" style="width: 100%;cursor:hand;" id="btn_readArgu" value="Get Arguments" disabled="disabled"/>
        </td>
        <td align="right">
          <label>
            <input type="button" style="width: 100%;cursor:hand;" id="btn_submit" value="TEST START" />
          </label>
        </td>
        <td>
          <input type="button" style="width: 100%;cursor:hand;" id="btn_showlist" value="Show List" />
      </tr>
      <tr>
        <td height="40" class="bottom"><font class="respon" style="font-size: 15;">응답 메세지</font></td>
        <td class="bottom"><input id="erCode" readonly="readonly" style="border: 1 1 1 1;border-style: solid;border-color: #AAA;background-color:#CEC8B9;"/></td>
        <td colspan="2" class="bottom" >
          <input id="erMessage" readonly="readonly"  size="100" style="border: 1 1 1 1;border-style: solid;border-color: #AAA;background-color:#CEC8B9;"/>
        </td>
      </tr>
    </table>
    <div style="display:none">
      <textarea id="textarea1" cols="32" rows="19"></textarea>
      <textarea id="textarea2" cols="32" rows="19"></textarea>
    </div>

  </form>
</div>

<div id="list" align="center" ">
<p style="margin-top: 10px; margin-bottom: 20px; font-size: 20;"><b><br>리스트 보기</b></p>
<div id="show_list" style="width: 1200;height: 600; overflow: scroll;">

</div>
<input type="button" style="margin-top: 10px; width: 200px;" id="btn_hidelist" value="Close"/>
</div>

</body>


</html>
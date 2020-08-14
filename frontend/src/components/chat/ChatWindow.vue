<template>
    <div>
        <div
            id="chat-page"
        >
            <div class="chat-container">
                <div class="chat-header">
                    <h2>
                        싸피고둥 채팅방
                        <button 
                            class="dark"
                            @click="close"
                        >
                            나가기
                        </button>
                    </h2>
                </div>
                <ul id="messageArea">
                    <li 
                        v-for="(item,index) in receiveList"
                        :key="index"
                    >
                        <template v-if="item.type==='CHAT'">
                            <div class="chat-message">
                                <p><strong>{{ item.sender }}</strong> : {{ item.content }}</p>
                            </div>
                        </template>
                        <template v-else>
                            <div class="event-message">
                                <p>{{ item.content }}</p>
                            </div>
                        </template>
                    </li>
                </ul>
                <div
                    id="messageForm"
                    name="messageForm"
                >
                    <div class="form-group">
                        <div class="input-group clearfix">
                            <input 
                                id="message"
                                v-model="message" 
                                type="text" 
                                name="message"
                                class="form-control"
                            >
                            <button
                                class="primary"
                                @click="sendMessage"
                            >
                                작성
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { mapGetters } from 'vuex';
export default {
    props:{
        closeHandler:{
            type:Function,
            required:true
        },
        status:{
            type:Boolean,
            required:true
        }
    },
    data() {
        return {
            message:'',
            stompClient:null,
            receiveList:[]
        };
    },
    computed: {
        ...mapGetters(['nickname']),
    },
    created() {
        this.connect();
    },
    methods: {
        connect(){
            const serverURL = "http://localhost:8399/ws";
            let socket = new SockJS(serverURL);
            this.stompClient = Stomp.over(socket);
            this.stompClient.connect({},this.onConnected,this.onError);
        },
        onConnected(){
            this.stompClient.subscribe('/topic/public',this.onMessageReceived);
            this.stompClient.send("/chat/addUser",
                JSON.stringify({
                    sender:this.nickname,
                    type:'JOIN',
                    content:''
                }),
                {},
                
            );
        },
        onError(error){
            console.log(error);
        },
        sendMessage(){
            if(this.message.length > 0 && this.stompClient){
                console.log("메세지 보냄");
                var chatMessage = {
                    sender:this.nickname,
                    content:this.message,
                    type:"CHAT"
                };
                this.stompClient.send("/chat/sendMessage",JSON.stringify(chatMessage),{});
                this.message ='';
            }
        },
        onMessageReceived(payload){
            var content = JSON.parse(payload.body);
            if(content.type==="JOIN"){
                content.content = content.sender+" 님이 참가했습니다.";
            }else if(content.type==="LEAVE"){
                content.content = content.sender+" 님이 퇴장했습니다.";
            }
            this.receiveList.push(content);
        },
        leaved(){
            if(this.stompClient){
                var chatMessage = {
                    sender:this.nickname,
                    content:'',
                    type:"LEAVE"
                };
                this.stompClient.send("/chat/sendMessage",JSON.stringify(chatMessage),{});
                this.stompClient.disconnect();
            }
            this.receiveList = [];
        },
        close(){
            this.leaved();
            this.closeHandler();
        }
    },
};
</script>

<style>
#chat-page{
    position: relative;
    width: 350px;
    height: 300px;
}
.clearfix:after {
    display: block;
    content: "";
    clear: both;
}
.form-control {
    width: 100%;
    min-height: 30px;
    font-size: 15px;
    border: 1px solid #c8c8c8;
}
input {
    padding-left: 10px;
    outline: none;
}
h2{
    margin-top: 20px;
    margin-bottom: 20px;
}
button {
    box-shadow: none;
    border: 1px solid transparent;
    font-size: 14px;
    outline: none;
    line-height: 100%;
    white-space: nowrap;
    vertical-align: middle;
    border-radius: 2px;
    transition: all 0.2s ease-in-out;
    cursor: pointer;
    min-height: 30px;
}
button.primary {
    background-color: #128ff2;
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
    color: #fff;
}
button.dark{
    margin-left: 20px;
    font-size: 14px;
    height: 20px;
    width: 60px;
    background-color: black;
    box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
    color: #fff;
}
.chat-container {
    max-width: 700px;
    margin-left: auto;
    margin-right: auto;
    background-color: #fff;
    box-shadow: 0 1px 11px rgba(0, 0, 0, 0.27);
    margin-top: 30px;
    height: calc(100% - 60px);
    max-height: 600px;
    position: relative;
}
#chat-page ul {
    list-style-type: none;
    background-color: #FFF;
    margin: 0;
    text-align: left;
    overflow-y: scroll;
    padding: 0 20px 0px 20px;
    height: calc(100% - 120px);
}
#chat-page #messageForm {
    padding: 20px;
}
#chat-page ul li {
    line-height: 1.5rem;
    padding: 10px 20px;
    margin: 0;
    border-bottom: 1px solid #f4f4f4;
}

#chat-page ul li p {
    margin: 0;
}
#chat-page .event-message {
    width: 100%;
    clear: both;
}

#chat-page .event-message p {
    color: #777;
    font-size: 14px;
    word-wrap: break-word;
    
}
#chat-page .chat-message {
    position: relative;
    width: 100%;
    word-break:break-all;
}

#chat-page .chat-message span {
    color: #333;
    font-weight: 600;
}

#chat-page .chat-message p {
    color: #43464b;
}

.input-group input {
    float: left;
    height: 30px;
    width: calc(100% - 85px);
}
.input-group button {
    float: left;
    width: 80px;
    height: 20px;
    margin-left: 5px;
    position: relative;
    bottom: -10px;
}
@media screen and (max-width: 730px) {
    .chat-container {
        margin-left: 10px;
        margin-right: 10px;
        margin-top: 10px;
    }
}

@media screen and (max-width: 480px) {
    .chat-container {
        height: calc(100% - 30px);
    }
    #chat-page ul {
        height: calc(100% - 120px);
    }

    #messageForm .input-group button {
        width: 65px;
    }

    #messageForm .input-group input {
        width: calc(100% - 70px);
    }
}
</style>
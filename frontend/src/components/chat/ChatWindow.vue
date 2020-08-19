<template>
    <div>
        <div
            id="chat-page"
        >
            <!-- <div class="chat-container"> -->
            <div id="chat-header">
                <span class="title">{{ $t('chat.title') }} ({{ userCount }}명 참여)</span>
                <button 
                    class="button-close button"
                    @click="close"
                >
                    {{ $t('chat.exit') }}
                </button>
            </div>
            <ul id="message-area">
                <li 
                    v-for="(item,index) in receiveList"
                    :key="index"
                >
                    <template v-if="item.type==='CHAT'">
                        <div class="chat-message message">
                            <div>
                                <span><strong>{{ item.sender }}</strong> : </span>
                                <span>{{ item.content }} </span>
                                <span class="time">{{ item.time }}</span>
                            </div>
                        </div>
                    </template>
                    <template v-else>
                        <div class="event-message message">
                            <div><strong>{{ item.content }}</strong>{{ $t(item.event) }}</div>
                        </div>
                    </template>
                </li>
            </ul>
            <div id="message-form">
                <input
                    v-model="message"
                    class="input-send"
                    autocomplete="off"
                    type="text"
                    @keyup.enter="sendMessage"
                >
                <button
                    class="button-send"
                    @click="sendMessage"
                >
                    {{ $t('chat.send') }}
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import { makeDraggable } from '@/components/chat/draggable';
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { mapGetters } from 'vuex';
import moment from 'moment';
import constant from '@/constant';

export default {
    props: {
        closeHandler: {
            type: Function,
            required: true
        },
        status: {
            type: Boolean,
            required: false,
            default: false,
        }
    },
    data: () => ({
        message: '',
        stompClient: null,
        receiveList: [],
        userCount: 0
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    created() {
        this.connect();
    },
    mounted() {
        makeDraggable('chat-page', 'chat-header');
        
        window.onbeforeunload = () => {
            this.close();

            return undefined;
        };
    },
    methods: {
        connect() {
            const serverURL = `${constant.baseURL}/ws`;
            let socket = new SockJS(serverURL);
            this.stompClient = Stomp.over(socket);
            this.stompClient.hasDebug = false;
            this.stompClient.connect({}, this.onConnected, this.onError);
        },
        onConnected() {
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
        onError(error) {
            console.log(error);
        },
        sendMessage(element) {
            if(this.message.length > 0 && this.stompClient) {
                var chatMessage = {
                    sender:this.nickname,
                    content:this.message,
                    type:"CHAT"
                };
                this.stompClient.send("/chat/sendMessage", JSON.stringify(chatMessage),{});
                this.message ='';
            }
        },
        getFormatDate(date) {
            return moment(new Date(date)).format("HH:mm");
        },
        onMessageReceived(payload) {
            if (!this.stompClient)
                return;
            const messageAreaElement = document.getElementById('message-area');
            const oldPosition = messageAreaElement.scrollTop;
            const oldHeight = messageAreaElement.scrollHeight - messageAreaElement.clientHeight;

            const isBottom = oldPosition === oldHeight;

            var content = JSON.parse(payload.body);

            switch(content.type) {
            case 'JOIN':
                content.content = content.sender;
                content.event = 'chat.message.enter';
                break;
            case 'LEAVE':
                content.content = content.sender;
                content.event = 'chat.message.leave';
                break;
            case 'CHAT':
                content.time = `${this.getFormatDate(Date.now())}`;
            }
            this.userCount = content.count;
            this.receiveList.push(content);

            
            setTimeout(() => {
                if (isBottom) { // 최하단에 있을 때 스크롤 동작
                    messageAreaElement.scrollTop = messageAreaElement.scrollHeight - messageAreaElement.clientHeight;
                }
            }, 125); // receiveList에 채팅이 추가된 후 DOM에 반영되는데 걸리는 시간 대기
        },
        leaved() {
            if (this.stompClient) {
                var chatMessage = {
                    sender:this.nickname,
                    content:'',
                    type:"LEAVE"
                };
                this.stompClient.send("/chat/leaveUser",JSON.stringify(chatMessage),{});
                this.stompClient.disconnect();
            }
            this.receiveList = [];
        },
        close() {
            this.leaved();
            this.closeHandler();
        }
    },
};
</script>

<style lang="scss" scoped>
button {
    box-shadow: none;
    border: none;
    line-height: 100%;
    border-radius: 4px;
    transition: all 0.2s ease-in-out;
    cursor: pointer;
    min-height: 30px;
}
button:hover {
    opacity: 0.9;
    transition: 0.5s;
}
button:active {
    -webkit-transform: scale(1.05);
    transform: scale(1.05);
    transition: 0.1s;
}

#chat-page {
    position: fixed;
    z-index: 11;
    left: 68px;
    top: 180px;
    border-radius: 2px;
    width: 350px;
    height: 400px;
    display: flex;
    flex-direction: column;
    box-shadow: 0 1px 11px rgba(0, 0, 0, 0.27);
}

#chat-header {
    height: 50px;
    padding: 3px;
    background-color: #0363BA;
    color: whitesmoke;
    cursor: move;
    display: flex;
    align-items: center;

    .title {
        flex: 1;
        margin: 5px 10px;
        font-size: 140%;
        text-align: left;
    }

    .button-close {
        width: 60px;
        background-color: black;
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
        color: #fff;
        margin-right: 4px;
    }
}

#message-area {
    flex: 1;
    background-color: white;
    max-height: calc(400px - 50px - 64px);
    margin: 0;
    text-align: left;
    padding-top: 4px;
    padding-left: 12px;
    padding-right: 12px;
    overflow: auto;

    li {
        margin: 2px 2px;
        border-bottom: 1px solid whitesmoke;
    }

    .event-message {
        background-color: gainsboro;
        padding: 0px 2px;
        text-align: center;
        border-radius: 4px;
    }
    .message {
        .time {
            color: gray;
            font-size: 12px;
        }
    }
}

#message-form {
    height: 64px;
    display: flex;
    flex-direction: row;
    align-items: center;
    background-color: white;

    padding: 8px 8px 4px 8px;

    .input-send {
        height: 30px;
    }

    .button-send {
        height: 30px;
        margin-left: 8px;
        width: 80px;
        background-color: #0363BA;
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
        color: #fff;
    }
}

@media (max-width: 992px) {
    #chat-page {
        z-index: 11;
        left: 0px;
        top: 0px;
        border-radius: 2px;
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        box-shadow: 0 1px 11px rgba(0, 0, 0, 0.27);
    }

    #message-area {
        flex: 1;
        background-color: white;
        max-height: 100%;
        margin: 0;
        text-align: left;
        padding-top: 4px;
        padding-left: 12px;
        padding-right: 12px;
        overflow: auto;
    }
}
</style>
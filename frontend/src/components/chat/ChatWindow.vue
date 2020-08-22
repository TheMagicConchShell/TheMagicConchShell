<template>
    <div>
        <div
            id="chat-page"
            :class="{shadow: !minified}"
        >
            <div
                id="chat-header"
                @mousedown.self="drag"
            >
                <span
                    class="title"
                    @mousedown="drag"
                >
                    {{ $t('chat.title') }}<span class="count">({{ userCount }}명 참여)</span>
                </span>
                <button 
                    class="button-minify button"
                    @click="minify"
                >
                    <template v-if="minified">
                        +
                    </template>
                    <template v-else>
                        -
                    </template>
                </button>
                <button 
                    class="button-close button"
                    @click="close"
                >
                    X
                </button>
            </div>
            <ul
                v-show="!minified"
                id="message-area"
            >
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
            <div
                v-show="!minified"
                id="message-form"
            >
                <input
                    ref="MessageInputArea"
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
import { makeDraggable } from '@/util/draggable';
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import { mapGetters } from 'vuex';
import moment from 'moment';

export default {
    props: {
        closeHandler: {
            type: Function,
            required: true,
        },
        status: {
            type: Boolean,
            required: false,
            default: false,
        },
    },
    data: () => ({
        message: '',
        stompClient: null,
        receiveList: [],
        userCount: 0,
        minified: false,
        minifiedTop: 0,
        minifiedLeft: 0,
        minifyInterval: null,
    }),
    computed: {
        ...mapGetters(['nickname']),
    },
    created() {
        this.connect();
    },
    beforeDestroyed() {
        if (this.minifyInterval) {
            clearInterval(this.minifyInterval);
        }
    },
    mounted() {
        makeDraggable('chat-page', 'chat-header');

        if (this.$refs.MessageInputArea) {
            this.$refs.MessageInputArea.focus();
        }
        
        window.onbeforeunload = () => {
            this.close();

            return undefined;
        };
    },
    methods: {
        connect() {
            const serverURL = `${process.env.VUE_APP_API_URL}/ws`;
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
        },
        sendMessage(element) {
            if(this.message.length > 0 && this.stompClient) {
                if (!this.stompClient)
                    return;

                const chatMessage = {
                    sender:this.nickname,
                    content:this.message,
                    type:"CHAT"
                };

                this.stompClient.send("/chat/sendMessage", JSON.stringify(chatMessage),{});
                this.message ='';

                const messageAreaElement = document.getElementById('message-area');
                setTimeout(() => {
                    messageAreaElement.scrollTop = messageAreaElement.scrollHeight - messageAreaElement.clientHeight;
                }, 125); // receiveList에 채팅이 추가된 후 DOM에 반영되는데 걸리는 시간 대기
            }
        },
        getFormatDate(date) {
            return moment(new Date(date)).format("HH:mm");
        },
        onMessageReceived(payload) {
            if (!this.stompClient)
                return;

            const content = JSON.parse(payload.body);

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

            const messageAreaElement = document.getElementById('message-area');
            
            if (messageAreaElement) {
                const oldPosition = messageAreaElement.scrollTop;
                const oldHeight = messageAreaElement.scrollHeight - messageAreaElement.clientHeight;

                const isBottom = oldPosition === oldHeight;

                setTimeout(() => {
                    if (isBottom) { // 최하단에 있을 때 스크롤 동작
                        messageAreaElement.scrollTop = messageAreaElement.scrollHeight - messageAreaElement.clientHeight;
                    }
                }, 125); // receiveList에 채팅이 추가된 후 DOM에 반영되는데 걸리는 시간 대기
            }
        },
        leaved() {
            if (this.stompClient) {
                const chatMessage = {
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
        },
        drag() {
            this.minified = false;
            clearInterval(this.minifyInterval);
        },
        minify() {
            const chatElement = document.getElementById('chat-page');

            if (this.minified) {
                this.minified = false;

                chatElement.style.top = this.minifiedTop;
                chatElement.style.left = this.minifiedLeft;

                clearInterval(this.minifyInterval);
            } else {
                this.minified = true;

                this.minifiedTop = chatElement.style.top;
                this.minifiedLeft = chatElement.style.left;

                const headerElement = document.getElementById('chat-header');

                const minifyTop = document.documentElement.clientHeight - headerElement.clientHeight;
                const minifyLeft = 0;
                
                const same = function(a, b) {
                    return (a <= b + 1) && (a >= b - 1);
                };

                this.minifyInterval = setInterval(() => {
                    const nextTop = `${(chatElement.offsetTop + minifyTop) / 2}px`;
                    const nextLeft = `${(chatElement.offsetLeft + minifyLeft) / 2}px`;

                    chatElement.style.top = nextTop;
                    chatElement.style.left = nextLeft;

                    if (same(chatElement.offsetTop, minifyTop) && same(chatElement.offsetLeft, minifyLeft)) {
                        chatElement.style.top = `${minifyTop}px`;
                        chatElement.style.left = `${minifyLeft}px`;

                        clearInterval(this.minifyInterval);
                    }
                }, 17);
            }
        },
    },
};
</script>

<style lang="scss" scoped>
.button {
    min-height: 30px;
    width: 40px;
    
    background-color: transparent;

    box-shadow: none;
    border: none;
    line-height: 100%;
    border-radius: 4px;
    transition: all 0.2s ease-in-out;
    cursor: pointer;
    color: #fff;
    margin-right: 4px;
}

.button-minify {
    font-size: 140%;
}

.button:hover {
    background-color: black;
    background-color: rgba(0, 0, 0, 0.33);
    // opacity: 0.2;
    transition: 0.5s;
}
.button:active {
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
    width: 450px;
    height: 400px;
    display: flex;
    flex-direction: column;
}
.shadow {
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
        box-shadow: none;
        border: none;
        border-radius: 4px;
        height: 30px;
        margin-left: 8px;
        width: 80px;
        background-color: #0363BA;
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
        color: #fff;
    }
}
.count {
    font-size: 100%;
}
@media (max-width: 768px) {
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

    .count {
        font-size: 80%;
    }
}
</style>
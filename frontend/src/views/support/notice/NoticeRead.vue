<template>
    <div class="content">
        <router-link
            :to="{name: 'NoticeList'}"
            tag="button"
            class="button"
        >
            목록
        </router-link>
        <div class="view">
            <div class="title">
                {{ title }}
            </div>

            <div class="informations">
                <div class="information flex-1 text-left">
                    {{ writer }}
                </div>
                <div class="information flex-1 text-right">
                    {{ writeDate }}
                </div>
            </div>

            <div class="content">
                <viewer
                    v-if="content"
                    :initial-value="content"
                />
            </div>
        </div>
        
        <write-button
            v-if="nickname && nickname === 'admin'"
            class="button float-right"
            :handler="moveUpdate"
            :text="'수정'"
        />
    </div>
</template>

<script>
import moment from 'moment';
import { mapGetters } from 'vuex';
import WriteButton from "@/components/WriteButton";

export default {
    components: {
        WriteButton
    },
    props: {
        no: {
            type: Number,
            required: true,
        }
    },
    data: () => ({
        writer: "",
        writeDate: "",
        title:  "",
        content: null,
    }),
    computed: {
        ...mapGetters([
            'language',
            'nickname',
        ]),
    },
    async created() {
        const response = await this.$axios({
            url: 'support/notice/noticeId',
            method: "get",
            params: {
                id: this.no
            }
        }).catch(() => {
            this.$router.push('Error');
        });

        if (response) {
            console.log(response);
            if (response.status == 200) {
                this.writer = '관리자'; //response.data.data.writer;
                this.writeDate = this.formatDate(response.data.data.writeDate);
                this.title = response.data.data.title;
                this.content = response.data.data.content;
            }
        }
    },
    methods: {
        formatDate(date) {
            return moment(new Date(date)).format("YYYY.MM.DD HH:mm:ss");
        },
        moveUpdate() {
            this.$router.push({
                name: 'NoticeUpdate',
                params: {
                    no: this.no,
                }
            });
        },
    }
};
</script>

<style scoped>
div, button {
    margin: 8px;
    text-align: left;
}
.button {
    background-color: #0363BA;
    color: whitesmoke;
    border: unset;
    border-radius: 4px;
    padding: 3px 12px;
}
.button:hover {
    color: gray;
}

.title {
    border-bottom: gray 1px dotted;
    font-weight: bold;
}

.informations {
    font-size: 90%;
    display: flex;
    margin: 0 auto;
}
.information {
    margin-top: 0px;
}

.view {
    border: gray 1px solid;
}

.flex-1 {
    flex: 1;
}
</style>
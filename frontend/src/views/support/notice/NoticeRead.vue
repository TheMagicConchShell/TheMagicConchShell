<template>
    <div class="wrapper">
        <router-link
            :to="{name: 'NoticeList'}"
            tag="button"
            class="button"
        >
            목록
        </router-link>
        <div class="notice-view">
            <div class="notice-title">
                {{ title }}
            </div>

            <div class="notice-informations">
                <div class="information flex-1 text-left">
                    {{ writer }}
                </div>
                <div class="information flex-1 text-right">
                    {{ writeDate }}
                </div>
            </div>

            <div class="notice-content">
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
import { formatDate } from '@/util/format';
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
            this.$toast('안내', '해당 공지사항이 존재하지 않습니다.');
            this.$router.push({ name: 'NoticeList' });
        });

        if (response) {
            if (response.status == 200) {
                this.writer = '관리자'; //response.data.data.writer;
                this.writeDate = formatDate(response.data.data.writeDate);
                this.title = response.data.data.title;
                this.content = response.data.data.content;
            }
        }
    },
    methods: {
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

<style lang="scss" scoped>
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

.flex-1 {
    flex: 1;
}

.notice-view {
    border: gray 1px solid;
    
    .notice-title {
        padding-bottom: 4px;
        border-bottom: gray 1px dotted;
        font-weight: bold;
        font-size: 1.2rem;
    }

    .notice-informations {
        display: flex;
        margin: 0 auto;
        .information {
            margin-top: 0px;
        }
    }
}
</style>
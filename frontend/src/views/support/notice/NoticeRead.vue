<template>
    <div class="content">
        <router-link
            :to="{name: 'NoticeList'}"
            tag="button"
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
            class="button float-right"
            :handler="moveUpdate"
            :text="'수정'"
        />
    </div>
</template>

<script>
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
                this.writer = response.data.data.writer;
                this.writeDate = response.data.data.writeDate;
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

<style scoped>
div, button {
    margin: 8px;
    text-align: left;
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
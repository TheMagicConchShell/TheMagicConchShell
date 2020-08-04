<template>
    <base-editor
        :submit-url="'/support/notice'"
        :submit-method="'put'"
        :default-id="id"
        :default-title="title"
        :default-content="content"
        :default-writer="writer"
    />
</template>

<script>
import BaseEditor from '@/components/BaseEditor';

export default {
    name: 'NoticeUpdate',
    components: {
        BaseEditor,
    },
    props: {
        no: {
            type: Number,
            required: true,
        }
    },
    data: () => ({
        id: 0,
        writer: "",
        title:  "",
        content: "",
    }),
    async created() {
        const response = await this.$axios({
            url: 'support/notice/noticeId',
            method: "get",
            params: {
                id: this.no
            }
        }).catch(() => {});

        if (response) {
            console.log(response);
            if (response.status == 200) {
                this.id = response.data.data.nid;
                this.writer = response.data.data.writer;
                this.writeDate = "1234";
                this.title = response.data.data.title;
                this.content = response.data.data.content;
            }
        }

        console.log("end");
    }
};
</script>

<style>

</style>
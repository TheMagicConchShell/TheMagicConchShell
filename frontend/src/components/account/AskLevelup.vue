<template>
    <div>
        <b-modal
            id="ask-level-up"
            ref="modal"
            hide-header
            hide-footer
        >
            <hr>
            <div id="level-up-text">
                <h1>LEVEL UP</h1>
                포인트 {{ levelupRequirePoint }}을 소모하여 Lv.{{ level+1 }}로 레벨업 합니다.
            </div>
            <hr>
            <b-button
                id="level-up-ok"
                block
                pill
                variant="outline-primary"
                class="float-right"
                @click="dolevelup"
            >
                레벨업
            </b-button>
            <div id="cancel-btn">
                <b-button
                    pill
                    variant="link"
                    @click="$bvModal.hide('ask-level-up')"
                >
                    <i class="fas fa-times" />
                </b-button>               
            </div>
        </b-modal>
    </div>
</template>

<script>
export default {
    props: {
        level: {
            type: Number,
            required: true,
        },
        levelupRequirePoint: {
            type: Number,
            required: true,
        },
    },
    data: () => ({

    }),
    methods: {
        dolevelup() {
            this.$emit("levelup");
        }
    },
};
</script>

<style scoped>
#level-up-ok {
    margin-left: 0.5rem;
}
#cancel-btn {
    text-align: center;
}
#level-up-text {
    margin: 1rem 0 1rem 0;
    text-align: center;
}
</style>

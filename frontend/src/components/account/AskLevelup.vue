<template>
    <div>
        <b-modal
            id="ask-level-up"
            ref="modal"
            title="레벨업 하시겠습니까?"
            hide-footer
        >
            포인트 {{ levelupRequirePoint }}을 소모하여 Lv.{{ level+1 }}로 레벨업 합니다.
            <div>
                <b-button
                    id="level-up-ok"
                    variant="outline-primary"
                    class="float-right"
                    @click="dolevelup"
                >
                    레벨업
                </b-button>
                <b-button
                    variant="outline-secondary"
                    class="float-right"
                    @click="$bvModal.hide('ask-level-up')"
                >
                    취소
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
</style>

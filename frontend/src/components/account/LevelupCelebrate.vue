<template>
    <div>
        <b-modal
            id="level-up-celebrate"
            ref="modal"
            hide-header
            hide-footer
        >
            <div class="d-block text-center">
                <img
                    src="@/assets/images/level_up.gif"
                    style="max-width: 100%"
                    @click="$bvModal.hide('level-up-celebrate')"
                >
            </div>
        </b-modal>
    </div>
</template>

<script>
export default {
    data: () => ({

    }),
    methods: {
    },
};
</script>

<style scoped>
</style>

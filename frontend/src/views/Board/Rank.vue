<template>
    <div class="container">
        <div id="home">
            <transition name="conversion">
                <span :key="language">{{ $t('ranking.board') }}</span>
            </transition>
        </div>
        <b-tabs
            pills
        >
            <b-tab 
                :title="$t('ranking.type.user')" 
                title-link-class=""
                active
            >
                <RankingTable
                    :table-type="'user'"
                />
            </b-tab>
            <b-tab 
                :title="$t('ranking.type.counsel')"
                title-link-class=""
            >
                <RankingTable
                    :table-type="'post'"
                />
            </b-tab>
            <b-tab 
                :title="$t('ranking.type.reply')"
                title-link-class=""
            >
                <RankingTable
                    :table-type="'reply'"
                />
            </b-tab>
        </b-tabs>
    </div>
</template>

<script>
import RankingTable from '@/components/rank/RankingTable.vue';
import { mapState, mapGetters } from 'vuex';

export default {
    name: 'Rank',
    components:{
        RankingTable
    },
    data() {
        return {
            isActive: 'false'
        };
    },
    computed: {
        ...mapGetters(['language']) 
    },
    methods: {
        click1() {

        }
    }
};
</script>

<style scoped>
</style>
<template>
    <div class="container">
        <h1>{{ $t('ranking.board') }}</h1>
        <b-tabs 
            active-nav-item-class="h3 font-weight-bold text-uppercase text-dark"
            content-class="mt-3"
            fill
        >
            <b-tab 
                :title="$t('ranking.type.user')" 
                title-link-class="h3"
                active
            >
                <RankingTable
                    :table-type="'user'"
                />
            </b-tab>
            <b-tab 
                :title="$t('ranking.type.counsel')"
                title-link-class="h3"
            >
                <RankingTable
                    :table-type="'post'"
                />
            </b-tab>
            <b-tab 
                :title="$t('ranking.type.reply')"
                title-link-class="h3"
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
export default {
    name: 'Rank',
    components:{
        RankingTable
    }
};
</script>

<style>

</style>
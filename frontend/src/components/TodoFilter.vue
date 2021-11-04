<template>
	<div
		class="
		flex
		justify-center
		items-center
		fixed
		w-screen
		h-screen
		z-50
		bg-gray-700 bg-opacity-75
		"
		@click.self="closeModal"
		>
		<div
			class="
			flex
			grid
      px-4
			w-1/3
			h-2/3
			fixed
			translate-x-1/2 translate-y-1/2
			bg-white
			z-60
			"
		>
			<div class="h-5/6">
				<div class="block border-t-2" id='status'>상태</div>
				<div class="block" id='status'>
					<input type="checkbox" id="New" value="New" v-model="filters.status">
					<label for="New">New</label>
					<input type="checkbox" id="접수" value="접수" v-model="filters.status">
					<label for="접수">접수</label>
					<input type="checkbox" id="진행" value="진행" v-model="filters.status">
					<label for="진행">진행</label>
					<input type="checkbox" id="완료" value="완료" v-model="filters.status">
					<label for="완료">완료</label>
					<br>
				</div>
				<div class="block border-t-2" id='status'>즐겨찾기</div>
				<div class="block" id='favorite'>
					<input type="checkbox" id="favorite" value="favorite" v-model="filters.favorite">
					<label for="favorite">favorite</label>
					<br>
				</div>
				<div class="block border-t-2" id='status'>팀</div>
				<div class="block" id='teams'>
					<div v-for="(team,index) in teamInfoList" :key="index">
						<input type="checkbox" :id="index" :value="team.teamName" v-model="filters.team">
						<label :for="index">{{team.teamName}}</label>
					</div>
					<br>
				</div>
			</div>
			<div class="flex justify-center items-center h-1/6 border-t-2 p-5">
        <button
          class="
						bg-green-600
            text-white text-base
            font-semibold
            w-20
            py-2
            px-4
            rounded-lg
            shadow-md
            hover:bg-green-800
            focus:outline-none
            focus:ring-2
            focus:ring-green-500
            focus:ring-offset-2
            focus:ring-offset-green-200
            mr-10
          "
					@click="applyFilter"
        >
          적용
        </button>
				<button
          class="
						bg-red-600
            text-white text-base
            font-semibold
            w-20
            py-2
            px-4
            rounded-lg
            shadow-md
            hover:bg-red-800
            focus:outline-none
            focus:ring-2
            focus:ring-red-500
            focus:ring-offset-2
            focus:ring-offset-red-200
            mr-10
          "
					@click="cleanFilter"
        >
          초기화
        </button>
        <button
          class="
            bg-blue-600
            text-white text-base
            font-semibold
            w-20
            py-2
            px-4
            rounded-lg
            shadow-md
            hover:bg-blue-800
            focus:outline-none
            focus:ring-2
            focus:ring-blue-500
            focus:ring-offset-2
            focus:ring-offset-blue-200
          "
          @click="closeModal"
        >
          닫기
        </button>
      </div>

		</div>
	</div>
</template>

<script>
	export default {
		name: 'TodoFilter',
		props:['teamInfoList'],
		data () {
			return {
				filters:{
					team:[],
					status:[],
					favorite:false,
				},
			}
		},
		methods: {
			applyFilter(){
				this.$emit('applyFilter',this.filters);
			},
			cleanFilter(){
				this.$emit('cleanFilter');
			},
			closeModal() {
      	this.$emit('closeModal');
    	},
		}
	}
</script>

<style>
</style>
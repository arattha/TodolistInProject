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
					<input type="checkbox" id="allStatus" v-model="selectAllStatus">
					<label for="allStatus">전체선택</label>

					<div v-for="(status,index) in statusList" :key="'s'+index">
						<input type="checkbox" :id="'s'+index" :value="status" v-model="filters.status">
						<label :for="'s'+index">{{status}}</label>
					</div>
					
					<br>
				</div>
				<div class="block border-t-2" id='teams'>팀</div>

				<input type="checkbox" id="allTeams" v-model="selectAllTeams">
				<label for="allTeams">전체선택</label>

				<div class="block" id='teams'>
					<div v-for="(team,index) in teamInfoList" :key="'t'+index">
						<input type="checkbox" :id="'t'+index" :value="team.teamName" v-model="filters.team">
						<label :for="'t'+index">{{team.teamName}}</label>
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
				statusList:['New','접수','진행','완료'],
			}
		},
		computed:{
			selectAllStatus: {
				get: function () {
					return this.statusList.length == this.filters.status.length ? true : false;
				},
				set: function (value) {
						if (value) {
								this.filters.status = this.statusList;
						} else {
							this.filters.status = [];
						}
				}
      },
			selectAllTeams: {
				get: function () {
					return this.teamInfoList.length == this.filters.team.length ? true : false;
				},
				set: function (value) {
						if (value) {
							this.teamInfoList.forEach(team => {
								this.filters.team.push(team.teamName);
							});
						} else {
							this.filters.team = [];
						}
				}
      },
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
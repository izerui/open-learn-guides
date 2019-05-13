<template>
    <v-app id="inspire" dark>
        <v-navigation-drawer
                v-model="drawer"
                clipped
                fixed
                app>
            <v-list dense>
                <v-list-tile v-for="item in menu" @click="goto(item.url)" :light="$route.path !== item.url">
                    <v-list-tile-action>
                        <v-icon>{{item.icon}}</v-icon>
                    </v-list-tile-action>
                    <v-list-tile-content>
                        <v-list-tile-title>
                                {{item.name}}
                        </v-list-tile-title>
                    </v-list-tile-content>
                </v-list-tile>
            </v-list>
        </v-navigation-drawer>
        <v-toolbar app fixed clipped-left>
            <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
            <v-toolbar-title>奥鹏助手</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-chip color="indigo" text-color="white">
                <v-avatar>
                    <v-icon>account_circle</v-icon>
                </v-avatar>
                {{opUser}}
                <v-btn icon>
                    <v-icon @click="logout">close</v-icon>
                </v-btn>
            </v-chip>
        </v-toolbar>
        <v-content>
            <v-container fluid fill-height>
                <router-view/>
            </v-container>
        </v-content>
        <v-footer app fixed>
            <span>&copy; 2019</span>
        </v-footer>
    </v-app>
</template>

<script>
    export default {
        name: "Main",
        data: () => ({
            drawer: null,
            opUser: '',
            menu: [
                {
                    name:'做作业',
                    icon: 'dashboard',
                    url: '/home',
                },
                {
                    name:'About',
                    icon: 'dashboard',
                    url: '/about',
                }
            ],
        }),
        created(){
            this.getUser();
        },
        methods: {
            logout() {
                this.$router.push("/login");
            },
            goto(url){
                this.$router.push(url);
            },
            getUser() {
                this.$fly.get('/userInfo').then((result) => {
                    this.opUser = result;
                });
            },
        }
    }
</script>

<style scoped>

</style>
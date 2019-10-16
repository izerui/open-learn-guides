<template>
    <v-app id="inspire">
        <v-content>
            <v-container fluid fill-height>
                <v-layout align-center justify-center>
                    <v-flex xs12 sm8 md4>
                        <v-card class="elevation-12">
                            <v-toolbar dark color="primary">
                                <v-toolbar-title>登录写作业</v-toolbar-title>
                            </v-toolbar>
                            <v-card-text>
                                <v-form>
                                    <v-text-field prepend-icon="person" name="username" label="Login" type="text" v-model="user.username"></v-text-field>
                                    <v-text-field id="password" prepend-icon="lock" name="password" label="Password" type="password" v-model="user.password"></v-text-field>
                                </v-form>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn color="primary" @click="submitBtn">Login</v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-content>
        <dialog-loader ref="dialogLoader"></dialog-loader>
    </v-app>
</template>

<script>
    import DialogLoader from "../components/DialogLoader";
    export default {
        components: {DialogLoader},
        data() {
            return {
                user: {
                    username: '',
                    password: '',
                },
            };
        },

        created() {},

        methods: {
            async submitBtn() {
                if(this.user && this.user.username && this.user.password){
                    this.$refs.dialogLoader.show( '登陆中...', { color: 'primary' } );
                    const result = await this.$fly.get("/login",this.user);
                    console.log(result);
                    this.$refs.dialogLoader.hide();
                    this.$router.push("/home");
                } else {
                    this.$refs.dialogLoader.showSnackbar( '请输入账号密码!', { color: 'error' } )
                }
            },
        },
    }
</script>
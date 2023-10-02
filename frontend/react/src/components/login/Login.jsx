'use client'

import {
    Button,
    Checkbox,
    Flex,
    Text,
    FormControl,
    FormLabel,
    Heading,
    Input,
    Stack,
    Image, Link, Box, Alert, AlertIcon,
} from '@chakra-ui/react'
import {Formik, Form, useField} from "formik";
import * as Yup from "yup";



const MyTextInput = ({ label, ...props }) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"}mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const LoginForm = () => {
    return(
        <Formik
            validateOnMount={true}
            initialValues={{username: '', password: ''}}
            validationSchema={
                Yup.object({
                    username: Yup.string().email("Must be valid email").required('Email is required'),
                    password: Yup.string().max(20,"Password cannot more than 20 characters").required('Password is required')
            })}
            onSubmit={(values,{setSubmitting})=>{
                alert(JSON.stringify(values,null,0))
            }}>

            {({isValid,isSubmitting})=>(
              <Form>
                    <Stack spacing={24}>
                        <MyTextInput
                        label={"Email"}
                        name={"username"}
                        type={"email"}
                        placeholder={"hello@gmail.com"}
                        />
                        <MyTextInput
                            label={"Password"}
                            name={"password"}
                            type={"password"}
                            placeholder={"********"}
                        />
                        <Button
                            type={"submit"}
                            disabled={!isValid || isSubmitting}>
                            Login
                        </Button>
                    </Stack>
                </Form>

                )}
        </Formik>
    )
}

const Login = () => {
    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'} >
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                        src={"https://unsplash.com/photos/dxS2okXd-zo/download?force=true&w=640"}
                        size={'200px'}
                        alt={'Login Image'}
                  />
                    <Heading fontSize={'2xl'}>Sign in to your account</Heading>
                   <LoginForm/>
                </Stack>
            </Flex>
            <Flex flex={1} p={10} flexDirection={"column"} alignItems={"center"} justifyContent={"center"} bgGradient={{sm:'linear(to-r, #010102, #010103)'}}>
                <Text fontSize={"4xl"} color={'white'} fontWeight={"black"} mb={5}>
                    <Link href={"https://github.com/Levantosina/inProgress"}>
                        A link to the project
                    </Link>

                </Text>
                <Image
                alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://unsplash.com/photos/o9XN28KdyN8/download?force=true&w=640'
                    }
                />
            </Flex>
        </Stack>
    )
}

export default Login;



// <FormControl id="email">
//     <FormLabel>Email address</FormLabel>
//     <Input type="email" />
// </FormControl>
// <FormControl id="password">
//     <FormLabel>Password</FormLabel>
//     <Input type="password" />
// </FormControl>
// <Stack spacing={6}>
//     <Stack
//         direction={{ base: 'column', sm: 'row' }}
//         align={'start'}
//         justify={'space-between'}>
//         <Checkbox>Remember me</Checkbox>
//         <Text color={'blue.500'}>Forgot password?</Text>
//     </Stack>
//     <Button colorScheme={'blue'} variant={'solid'}>
//         Sign in
//     </Button>
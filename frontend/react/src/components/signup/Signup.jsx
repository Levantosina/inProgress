import {useAuth} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {Flex, Heading, Image, Link, Stack, Text} from "@chakra-ui/react";
import CreateCustomerForm from "../shared/CreateCustomerForm.jsx";

const Signup = () => {
    const {customer,setCustomerFromToken}=useAuth();
    const navigate=useNavigate();
    useEffect(()=>{
        if(customer){
            navigate("/dashboard/customers");
        }
    })


    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'} >
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                        src={"https://unsplash.com/photos/dxS2okXd-zo/download?force=true&w=640"}
                        size={'200px'}
                        alt={'Login Image'}
                    />
                    <Heading fontSize={'2xl'} mb={15}>Register for an account</Heading>
                    <CreateCustomerForm onSuccess={(token)=>{
                    localStorage.setItem("access_token",token);
                        setCustomerFromToken();
                        navigate("/dashboard");
                    }}/>

                    <Link color={"blue.500"}href={"/"}>
                        Have an account? Login now.
                    </Link>
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
export default Signup;
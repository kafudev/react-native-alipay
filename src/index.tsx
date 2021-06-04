import { NativeModules } from 'react-native';

type AlipayType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Alipay } = NativeModules;

export default Alipay as AlipayType;

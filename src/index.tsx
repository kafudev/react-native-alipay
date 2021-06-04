import { NativeModules } from 'react-native';
import { Buffer } from 'buffer';
import RSASign from 'jsrsasign';

type TStatus = '9000' | '8000' | '4000' | '5000' | '6001' | '6002';

interface IResult {
  resultStatus: TStatus;
  result: string;
  memo: string;
}

type AlipayType = {
  multiply(a: number, b: number): Promise<number>;
  authWithInfo(infoStr: string): IResult;
  setAlipaySandbox(isSandbox: boolean): void;
  pay(infoStr: string): IResult;
  payInterceptorWithUrl(infoStr: string): {
    resultCode: TStatus;
    returnUrl: string;
  };
  sign(infoStr: string): string;
};

const { Alipay } = NativeModules;

Alipay.sign = (object: any, privateKey: string) => {
  // Add default sign_type
  if (!object.sign_type || object.sign_type.length === 0) {
    object.sign_type = 'RSA';
  }

  // Remove sign field
  delete object.sign;

  // Remove empty field
  Object.keys(object).forEach((key) => {
    if (String(object[key]).length === 0) {
      delete object[key];
    }
  });

  // Sort query string
  let sortedQuery = '';
  let sortedKeys = Object.keys(object).sort();
  for (var i = 0; i < sortedKeys.length; i++) {
    let key = sortedKeys[i];
    let value = object[key];
    sortedQuery += `${i === 0 ? '' : '&'}${encodeURIComponent(
      key
    )}=${encodeURIComponent(value)}`;
  }

  // Create signature
  let alg = '';
  if (object.sign_type === 'RSA') {
    alg = 'SHA1withRSA';
  }
  if (object.sign_type === 'RSA2') {
    alg = 'SHA256withRSA';
  }
  let sig = new RSASign.KJUR.crypto.Signature({ alg });
  sig.init(RSASign.KEYUTIL.getKey(privateKey));
  sig.updateString(sortedQuery);
  let sign = Buffer.from(sig.sign(), 'hex').toString('base64');

  sortedQuery += `&sign=${encodeURIComponent(sign)}`;
  return sortedQuery;
};

export default Alipay as AlipayType;

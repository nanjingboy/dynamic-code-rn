#import "DynamicViewController.h"
#import "ViewController.h"
#import "UIView+Toast.h"
#import "SSZipArchive.h"
#import "Masonry.h"

@interface ViewController ()

@property(nonatomic, strong) UIButton *v1_0_0;
@property(nonatomic, strong) UIButton *v1_0_1;
@property(nonatomic, strong) UIButton *v1_1_0;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    
    self.v1_0_0 = [UIButton new];
    self.v1_0_0.tag = 0;
    self.v1_0_0.backgroundColor = [UIColor colorWithRed:0.84 green:0.84 blue:0.84 alpha:1.00];
    [self.v1_0_0 setTitle:@"运行：V1.0.0" forState:UIControlStateNormal];
    [self.v1_0_0 addTarget:self action:@selector(runDynamicCode:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.v1_0_0];
    [self.v1_0_0 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.view).offset(10);
        make.right.equalTo(self.view).offset(-10);
        make.top.equalTo(self.view).offset(64);
        make.height.mas_equalTo(40);
    }];

    self.v1_0_1 = [UIButton new];
    self.v1_0_1.tag = 1;
    self.v1_0_1.backgroundColor = [UIColor colorWithRed:0.84 green:0.84 blue:0.84 alpha:1.00];
    [self.v1_0_1 setTitle:@"运行：V1.0.1" forState:UIControlStateNormal];
    [self.v1_0_1 addTarget:self action:@selector(runDynamicCode:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.v1_0_1];
    [self.v1_0_1 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.view).offset(10);
        make.right.equalTo(self.view).offset(-10);
        make.top.equalTo(self.v1_0_0.mas_bottom).offset(20);
        make.height.mas_equalTo(40);
    }];
    
    self.v1_1_0 = [UIButton new];
    self.v1_1_0.tag = 2;
    self.v1_1_0.backgroundColor = [UIColor colorWithRed:0.84 green:0.84 blue:0.84 alpha:1.00];
    [self.v1_1_0 setTitle:@"运行：V1.1.0" forState:UIControlStateNormal];
    [self.v1_1_0 addTarget:self action:@selector(runDynamicCode:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.v1_1_0];
    [self.v1_1_0 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.view).offset(10);
        make.right.equalTo(self.view).offset(-10);
        make.top.equalTo(self.v1_0_1.mas_bottom).offset(20);
        make.height.mas_equalTo(40);
    }];
}

- (void)runDynamicCode:(UIButton *)sender {
    NSString *version;
    if (sender.tag == 0) {
        version = @"1.0.0";
    } else if (sender.tag == 1) {
        version = @"1.0.1";
    } else {
        version = @"1.1.0";
    }
    NSString *sourceCodeUrl = [NSString stringWithFormat:@"http://127.0.0.1:8080/%@/ios.zip", version];
    [self.view makeToastActivity:CSToastPositionCenter];
    
    __weak __typeof(self)weakSelf = self;
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSURL *url = [NSURL URLWithString:sourceCodeUrl];
        NSData *data = [NSData dataWithContentsOfURL:url];
        __strong __typeof(weakSelf)strongSelf = weakSelf;
        if (data) {
            NSURL *distRootUrl = [strongSelf getDistFileUrl:version];
            NSURL *distZipUrl = [distRootUrl URLByAppendingPathComponent:@"ios.zip"];
            if ([data writeToURL:distZipUrl atomically:NO]) {
                BOOL upzipStatus = [SSZipArchive unzipFileAtPath:distZipUrl.path toDestination:distRootUrl.path];
                if (upzipStatus) {
                    [[NSFileManager defaultManager] removeItemAtURL:distZipUrl error:nil];
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [strongSelf downloadCallback:[distRootUrl URLByAppendingPathComponent:@"index.bundle"].path];
                    });
                } else {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [strongSelf downloadCallback:nil];
                    });
                }
            } else {
                dispatch_async(dispatch_get_main_queue(), ^{
                    [strongSelf downloadCallback:nil];
                });
            }
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                [strongSelf downloadCallback:nil];
            });
        }
    });
}

- (void)downloadCallback:(NSString *)filePath {
    [self.view hideToastActivity];
    if (filePath == nil) {
        [self.view makeToast:@"下载失败"];
    } else {
        DynamicViewController *controller = [DynamicViewController new];
        controller.jsSourcePath = filePath;
        [self presentViewController:controller animated:YES completion:nil];
    }
}

- (NSURL *)getDistFileUrl:(NSString *)version {
    NSURL *url = [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
    url = [url URLByAppendingPathComponent:version isDirectory:YES];
    NSFileManager *manager = [NSFileManager defaultManager];
    if ([manager fileExistsAtPath:url.path]) {
        [manager removeItemAtURL:url error:nil];
    }
    [manager createDirectoryAtURL:url withIntermediateDirectories:NO attributes:nil error:nil];
    return url;
}

@end

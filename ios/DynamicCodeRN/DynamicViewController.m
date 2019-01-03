#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>

#import "DynamicViewController.h"
#import "Masonry.h"

@interface DynamicViewController ()

@property(nonatomic, strong) UIButton *goBackBtn;
@property(nonatomic, strong) RCTRootView *reactRootView;

@end

@implementation DynamicViewController

-(void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];
    
    self.goBackBtn = [UIButton new];
    self.goBackBtn.tag = 0;
    self.goBackBtn.backgroundColor = [UIColor colorWithRed:0.84 green:0.84 blue:0.84 alpha:1.00];
    [self.goBackBtn setTitle:@"返回" forState:UIControlStateNormal];
    [self.goBackBtn addTarget:self action:@selector(goback) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.goBackBtn];
    [self.goBackBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.view).offset(10);
        make.right.equalTo(self.view).offset(-10);
        make.top.equalTo(self.view).offset(64);
        make.height.mas_equalTo(40);
    }];
    self.reactRootView = [[RCTRootView alloc] initWithBundleURL:[NSURL URLWithString:self.jsSourcePath]
                                                     moduleName:@"DynamicCodeRN"
                                              initialProperties:nil
                                                  launchOptions:nil];
    [self.view addSubview:self.reactRootView];
    [self.reactRootView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.goBackBtn.mas_bottom).offset(20);
        make.left.right.bottom.equalTo(self.view);
    }];
}

- (void)goback {
    [self dismissViewControllerAnimated:YES completion:nil];
}


@end
